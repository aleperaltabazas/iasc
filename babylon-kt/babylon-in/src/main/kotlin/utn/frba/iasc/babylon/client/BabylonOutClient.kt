package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CloseAuctionDTO

class BabylonOutClient(
    private val babylonOutConnector: Connector
) {
    fun newAuctionCallback(auctionId: String, timeout: Int, tags: List<String>) {
        val response = babylonOutConnector.post("/babylon-out/callback", CloseAuctionDTO(auctionId, timeout, tags))
        LOGGER.info(response.body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonOutClient::class.java)
    }
}