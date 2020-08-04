package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector

class BabylonStorageClient(
    private val babylonStorageConnector: Connector
) {
    fun close(id: String) {
        val response = babylonStorageConnector.post("/babylon-connector/auctions/$id/close")
        LOGGER.info(response.body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonStorageClient::class.java)
    }
}