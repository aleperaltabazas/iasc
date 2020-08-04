package utn.frba.iasc.babylon.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ClosedWithWinnerDTO::class, name = "CLOSED_WITH_WINNER"),
    JsonSubTypes.Type(value = ClosedUnresolvedDTO::class, name = "CLOSED_UNRESOLVED"),
    JsonSubTypes.Type(value = OpenDTO::class, name = "OPEN"),
    JsonSubTypes.Type(value = CancelledDTO::class, name = "CANCELLED")
)
sealed class StatusDTO(
    val type: String
)

object OpenDTO : StatusDTO("OPEN")

data class CancelledDTO(val cancelledOn: LocalDateTime) : StatusDTO("CANCELLED")

data class ClosedWithWinnerDTO(
    val closedOn: LocalDateTime,
    val winnerId: String,
    val losersId: List<String>,
    val finalPrice: Int
) : StatusDTO("CLOSED_WITH_WINNER")

data class ClosedUnresolvedDTO(
    val closedOn: LocalDateTime
) : StatusDTO("CLOSED_UNRESOLVED")