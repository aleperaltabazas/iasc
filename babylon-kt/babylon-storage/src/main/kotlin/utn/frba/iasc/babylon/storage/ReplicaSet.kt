package utn.frba.iasc.babylon.storage

import akka.actor.AbstractActor
import akka.actor.ActorRef
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.actor.*
import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import java.time.Duration

class ReplicaSet(
    private val objectMapper: ObjectMapper
) : AbstractActor() {
    private val babylonDataClients: MutableList<BabylonDataClient> = ArrayList()

    init {
        context.system.scheduler.scheduleWithFixedDelay(
            Duration.ZERO,
            Duration.ofMillis(5000),
            self,
            HealthCheck,
            context.system.dispatcher,
            ActorRef.noSender()
        )
    }

    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(Add::class.java) { add(it.host) }
            .match(All::class.java) { context.sender.tell(Clients(babylonDataClients), self) }
            .match(HealthCheck::class.java) { healthCheck() }
            .match(CreateAuctionDTO::class.java) { createAuction(it) }
            .match(PlaceBid::class.java) { placeBid(it.dto, it.auctionId) }
            .build()
    }

    private fun add(host: String) {
        val connector = Connector.create(objectMapper, host, ttl = 2)
        val client = BabylonDataClient(connector)
        this.babylonDataClients.add(client)

//        context.system.scheduler.scheduleOnce(
//            Duration.ofMillis(5000),
//            self,
//            HealthCheck,
//            context.system.dispatcher,
//            ActorRef.noSender()
//        )
    }

    private fun remove(host: String) {
        this.babylonDataClients.removeIf { it.host() == host }
    }

    private fun healthCheck() {
        LOGGER.info("Running health check...")

        babylonDataClients.removeIf { it.healthCheck().not() }
    }

    private fun createAuction(createAuction: CreateAuctionDTO) {
        babylonDataClients.forEach { it.createAuction(createAuction) }
    }

    private fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        babylonDataClients.forEach { it.placeBid(placeBid, auctionId) }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ReplicaSet::class.java)
    }
}