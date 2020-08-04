package utn.frba.iasc.babylon.dto

data class RegisterReplicaSetDTO(
    val host: String
)

data class ReplicaSetNeighboursDTO(
    val hosts: List<String>
)