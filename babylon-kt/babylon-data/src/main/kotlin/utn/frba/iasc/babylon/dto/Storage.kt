package utn.frba.iasc.babylon.dto

data class NewReplicaSetDTO(
    val host: String
)

data class ReplicaSetNeighboursDTO(
    val hosts: List<String>
)