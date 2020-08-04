package utn.frba.iasc.babylon.model

data class Buyer(
    override val id: String,
    val username: String,
    val ip: String,
    val interestTags: List<String>
) : Entity(id)