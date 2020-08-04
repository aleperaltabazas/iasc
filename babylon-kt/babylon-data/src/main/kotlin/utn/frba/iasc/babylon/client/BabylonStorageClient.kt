package utn.frba.iasc.babylon.client

import com.fasterxml.jackson.core.type.TypeReference
import org.slf4j.LoggerFactory
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.dto.NewReplicaSetDTO
import utn.frba.iasc.babylon.dto.ReplicaSetNeighboursDTO

class BabylonStorageClient(
    private val babylonStorageConnector: Connector
) {
    fun registerSelf(host: String): ReplicaSetNeighboursDTO {
        val response = babylonStorageConnector.post("/babylon-storage/replica-set", NewReplicaSetDTO(host))
        LOGGER.info(response.body)

        return response.deserializeAs(object : TypeReference<ReplicaSetNeighboursDTO>() {})
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BabylonStorageClient::class.java)
    }
}