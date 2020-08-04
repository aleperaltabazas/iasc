package utn.frba.iasc.babylon.client

import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.NewReplicaSetDTO

class BabylonStorageClient(
    private val babylonStorageConnector: Connector
) {
    fun registerSelf(host: String) {
        val response = babylonStorageConnector.post("/babylon-storage/replica-set", NewReplicaSetDTO(host))
        LOGGER.info(response.body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonStorageClient::class.java)
    }
}