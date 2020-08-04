package utn.frba.iasc.babylon.actor

import akka.actor.AbstractActor
import akka.japi.pf.ReceiveBuilder
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.client.BabylonStorageClient
import java.time.Duration

class CallbackActor(
    private val babylonStorageClient: BabylonStorageClient
) : AbstractActor() {
    override fun createReceive(): Receive = ReceiveBuilder.create()
        .match(DoClose::class.java) { close(it.id) }
        .match(CloseIn::class.java) { scheduleClose(it.id, it.timeout) }
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
        babylonStorageClient.close(auctionId)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CallbackActor::class.java)
    }
}