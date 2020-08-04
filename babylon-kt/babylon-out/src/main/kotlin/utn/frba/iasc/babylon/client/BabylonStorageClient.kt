package utn.frba.iasc.babylon.client

import com.fasterxml.jackson.core.type.TypeReference
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.StatusDTO
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Buyer

class BabylonStorageClient(
    private val babylonStorageConnector: Connector
) {
    fun find(id: String): Auction {
        val response = babylonStorageConnector.get("/babylon-storage/auctions/$id")

        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<Auction>() {})
    }

    fun update(status: StatusDTO, id: String) {
        val response = babylonStorageConnector.patch(
            path = "/babylon-storage/auctions/$id/status",
            body = status
        )

        LOGGER.info(response.body)
    }

    fun buyersInterestedIn(tags: List<String>): List<Buyer> {
        val response = babylonStorageConnector.get("/babylon-storage/buyers?likes=[${tags.joinToString(",")}]")

        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<List<Buyer>>() {})
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonStorageClient::class.java)
    }
}