package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CloseAuctionDTO

class BabylonOutClient(
    private val babylonOutConnector: Connector
) {
    fun scheduleClose(auctionId: String, timeout: Int) {
        val response = babylonOutConnector.post("/babylon-out/close-callback", CloseAuctionDTO(auctionId, timeout))
        LOGGER.info(response.body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonOutClient::class.java)
    }
}