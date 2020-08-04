package utn.frba.iasc.babylon.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

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


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CancelledDTO::class, name = "CANCELLED"),
    JsonSubTypes.Type(value = ClosedWithWinnerDTO::class, name = "CLOSED_WITH_WINNER"),
    JsonSubTypes.Type(value = ClosedUnresolvedDTO::class, name = "CLOSED_UNRESOLVED")
)
sealed class UpdateStatusDTO(
    val type: String
)

data class CancelledDTO(
    val cancelledOn: LocalDateTime
) : UpdateStatusDTO("CANCELLED")

data class ClosedWithWinnerDTO(
    val closedOn: LocalDateTime,
    val winnerId: String,
    val losersId: List<String>,
    val finalPrice: Int
) : UpdateStatusDTO("CLOSED_WITH_WINNER")

data class ClosedUnresolvedDTO(
    val closedOn: LocalDateTime
) : UpdateStatusDTO("CLOSED_UNRESOLVED")