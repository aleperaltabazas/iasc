package utn.frba.iasc.babylon.dto

data class StorageCreateAuctionDTO(
    val auctionId: String,
    val seller: String,
    val tags: List<String>,
    val basePrice: Int,
    val article: String
)

data class StorageCreateBuyerDTO(
    val buyerId: String,
    val username: String,
    val ip: String,
    val interestTags: List<String>
)