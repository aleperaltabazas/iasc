package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.InCreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.StorageCreateAuctionDTO
import utn.frba.iasc.babylon.dto.StorageCreateBuyerDTO

class BabylonStorageClient(
    private val babylonStorageConnector: Connector
) {
    fun createAuction(create: StorageCreateAuctionDTO) {
        val response = babylonStorageConnector.post(
            path = "/babylon-storage/auctions",
            body = create
        )

        LOGGER.info(response.body)
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        val response = babylonStorageConnector.put(
            path = "/babylon-storage/auctions/$auctionId/bids",
            body = placeBid
        )

        LOGGER.info(response.body)
    }

    fun createBuyer(create: StorageCreateBuyerDTO) {
        val response = babylonStorageConnector.post(
            path = "/babylon-storage/buyers",
            body = create
        )
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonStorageClient::class.java)
    }
}