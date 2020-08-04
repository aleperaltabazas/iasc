package utn.frba.iasc.babylon.client

import com.fasterxml.jackson.core.type.TypeReference
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Buyer

class BabylonDataClient(
    private val babylonDataConnector: Connector
) {
    fun askAuctions(): List<Auction> {
        val response = babylonDataConnector.get("/babylon-data/auctions")
        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<List<Auction>>() {})
    }

    fun askBuyers(): List<Buyer> {
        val response = babylonDataConnector.get("/babylon-data/buyers")
        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<List<Buyer>>() {})
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonDataClient::class.java)
    }
}