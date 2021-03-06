package utn.frba.iasc.babylon.storage

import akka.actor.AbstractActor
import akka.actor.ActorRef
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.actor.*
import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import java.time.Duration

class ReplicaSet(
    private val objectMapper: ObjectMapper
) : AbstractActor() {
    constructor(objectMapper: ObjectMapper, nodes: List<String>) : this(objectMapper) {
        nodes.forEach(this::add)
    }

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
            .match(CreateBuyerDTO::class.java) { createBuyer(it) }
            .match(UpdateStatus::class.java) { updateStatus(it) }
            .match(Any::class.java) { context.sender.tell(Client(babylonDataClients.random()), self) }
            .build()
    }

    private fun add(host: String) {
        val connector = Connector.create(objectMapper, host, ttl = 2)
        val client = BabylonDataClient(connector)
        this.babylonDataClients.add(client)
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

    private fun createBuyer(create: CreateBuyerDTO) {
        babylonDataClients.forEach { it.createBuyer(create) }
    }

    private fun updateStatus(updateStatus: UpdateStatus) {
        val (update, id) = updateStatus
        babylonDataClients.forEach { it.updateStatus(update, id) }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ReplicaSet::class.java)
    }
}