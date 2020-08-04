package utn.frba.iasc.babylon.client

import com.fasterxml.jackson.core.type.TypeReference
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.UpdateStatusDTO
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Buyer

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

    fun host(): String = babylonDataConnector.host

    fun healthCheck(): Boolean {
        return try {
            babylonDataConnector.get("/babylon-data/health-check").isError().not()
        } catch (e: Exception) {
            LOGGER.error("Health check failed for ${host()}", e)
            false
        }
    }

    fun createBuyer(create: CreateBuyerDTO) {
        val response = babylonDataConnector.post("/babylon-data/buyers", create)
        LOGGER.info(response.body)
    }

    fun updateStatus(update: UpdateStatusDTO, id: String) {
        val response = babylonDataConnector.patch("/babylon-data/auctions/$id/status", update)
        LOGGER.info(response.body)
    }

    fun find(id: String): Auction {
        val response = babylonDataConnector.get("/babylon-data/auctions/$id")
        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<Auction>() {})
    }

    fun buyers(tags: String?): List<Buyer> {
        val response = babylonDataConnector.get("/babylon-data/buyers?${tags ?: ""}")
        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<List<Buyer>>() {})
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonDataClient::class.java)
    }
}