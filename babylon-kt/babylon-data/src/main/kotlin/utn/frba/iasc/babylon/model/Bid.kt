package utn.frba.iasc.babylon.model

data class Bid(
    override val id: String,
    val offer: Int,
    val buyer: Buyer
) : Entity(id)