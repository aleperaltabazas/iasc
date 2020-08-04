package utn.frba.iasc.babylon.dto

data class InCreateAuctionDTO(
    val seller: String,
    val tags: List<String>,
    val basePrice: Int,
    val article: String
)

data class AuctionCreatedDTO(
    val auctionId: String
)

data class InCreateBuyerDTO(
    val username: String,
    val ip: String,
    val interestTags: List<String>
)

data class PlaceBidDTO(
    val buyer: String,
    val offer: Int
)