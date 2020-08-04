package utn.frba.iasc.babylon.dto

data class CloseAuctionDTO(
    val auctionId: String,
    val timeout: Int,
    val tags: List<String>
)