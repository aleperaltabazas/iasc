package utn.frba.iasc.babylon.dto

data class CreateAuctionDTO(
    val seller: String,
    val tags: List<String>,
    val basePrice: Int,
    val article: String
)

data class PlaceBidDTO(
    val buyer: String,
    val offer: Int
)

data class CreateBuyerDTO(
    val username: String,
    val ip: String,
    val interestTags: List<String>
)