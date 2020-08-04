package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.RegisterReplicaSetDTO
import utn.frba.iasc.babylon.dto.ReplicaSetNeighboursDTO
import utn.frba.iasc.babylon.service.GossipService

class GossipController(
    private val objectMapper: ObjectMapper,
    private val gossipService: GossipService
) : Controller {
    override fun register() {
        Spark.post("/babylon-storage/replica-set", "application/json", this::registerNeighbour, objectMapper::writeValueAsString)
    }

    private fun registerNeighbour(req: Request, res: Response): ReplicaSetNeighboursDTO {
        val hosts = gossipService.dataNeighbourHosts()

        val replicaSet: RegisterReplicaSetDTO = objectMapper.readValue(req.body())
        gossipService.register(replicaSet.host)

        return ReplicaSetNeighboursDTO(hosts)
    }
}