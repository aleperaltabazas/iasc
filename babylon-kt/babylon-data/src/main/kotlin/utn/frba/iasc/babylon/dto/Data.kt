package utn.frba.iasc.babylon.dto

data class CreateAuctionDTO(
    val seller: String,
    val tags: List<String>,
    val basePrice: Int,
    val article: String
)