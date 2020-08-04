package utn.frba.iasc.babylon.dto

data class NewAuctionCallbackDTO(
    val auctionId: String,
    val timeout: Int,
    val tags: List<String>
)