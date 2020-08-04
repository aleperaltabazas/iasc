package utn.frba.iasc.babylon.actor

import akka.actor.AbstractActor
import akka.japi.pf.ReceiveBuilder
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.client.BabylonStorageClient
import java.time.Duration
import java.time.LocalDateTime

class CallbackActor(
    private val babylonStorageClient: BabylonStorageClient
) : AbstractActor() {
    override fun createReceive(): Receive = ReceiveBuilder.create()
        .match(DoClose::class.java) { close(it.id) }
        .match(CloseIn::class.java) { scheduleClose(it.id, it.timeout) }
        .match(Inform::class.java) { inform(it.auctionId, it.tags) }
        .build()

    private fun scheduleClose(auctionId: String, timeout: Int) {
        LOGGER.info("Schedule $auctionId closing in $timeout seconds")

        context.system.scheduler.scheduleOnce(
            Duration.ofSeconds(timeout.toLong()),
            self,
            DoClose(auctionId),
            context.system.dispatcher,
            self
        )
    }

    private fun close(auctionId: String) {
        LOGGER.info("Close $auctionId")
        val auction = babylonStorageClient.find(auctionId)

        if (auction.isCancelled()) {
            LOGGER.info("Auction $auctionId is already cancelled")
        } else {

            babylonStorageClient.update(auction.closed(LocalDateTime.now()).status.toDTO(), auctionId)
        }
    }

    private fun inform(auctionId: String, tags: List<String>) {
        val buyers = babylonStorageClient.buyersInterestedIn(tags)
        buyers.forEach {
            LOGGER.info("Hey, ${it.username} - check out this awesome auction: $auctionId")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CallbackActor::class.java)
    }
}