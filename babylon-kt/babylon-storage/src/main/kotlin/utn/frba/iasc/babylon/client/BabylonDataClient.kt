package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO

class BabylonDataClient(
    private val babylonDataConnector: Connector
) {
    fun createAuction(createAuction: CreateAuctionDTO) {
        val response = babylonDataConnector.post("/babylon-data/auctions", createAuction)
        LOGGER.info(response.body)
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        val response = babylonDataConnector.put("/babylon-data/auctions/$auctionId/bids", placeBid)
        LOGGER.info(response.body)
    }

    companion object{
        private val LOGGER = LoggerFactory.getLogger(BabylonDataClient::class.java)
    }
}