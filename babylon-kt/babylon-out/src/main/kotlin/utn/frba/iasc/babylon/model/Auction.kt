package utn.frba.iasc.babylon.model

import utn.frba.iasc.babylon.dto.*
import java.time.LocalDateTime

data class Auction(
    override val id: String,
    val article: String,
    val status: AuctionStatus,
    val basePrice: Int,
    val bids: List<Bid>,
    val seller: String
) : Entity(id) {
    fun isOpen(): Boolean = status == Open

    fun closed(date: LocalDateTime): Auction = copy(status = status.closed(date, bids, basePrice))

    fun placeBid(bid: Bid): Auction = if (status.canBid()) copy(bids = bids + bid)
    else throw IllegalStateException("Can't bid on a closed or cancelled auction.")

    fun cancelled(date: LocalDateTime): Auction = copy(status = status.cancel(date))
}

sealed class AuctionStatus(val status: String) {
    abstract fun toDTO(): StatusDTO

    fun canBid(): Boolean = this == Open

    abstract fun closed(date: LocalDateTime, bids: List<Bid>, basePrice: Int): AuctionStatus

    abstract fun cancel(date: LocalDateTime): AuctionStatus

}

object Open : AuctionStatus("OPEN") {
    override fun toDTO(): StatusDTO = OpenDTO

    override fun closed(date: LocalDateTime, bids: List<Bid>, basePrice: Int): AuctionStatus =
        bids.filter { it.offer >= basePrice }
            .maxBy { it.offer }
            ?.let {
                ClosedWithWinner(
                    closedOn = date,
                    finalPrice = it.offer,
                    winner = it.buyer,
                    losers = bids.map { b -> b.buyer }.filterNot { b -> b == it.buyer }
                )
            }
            ?: ClosedUnresolved(date)

    override fun cancel(date: LocalDateTime): AuctionStatus = Cancelled(date)
}

data class Cancelled(val cancelledOn: LocalDateTime) : AuctionStatus("CANCELLED") {
    override fun toDTO(): StatusDTO = CancelledDTO(cancelledOn)

    override fun closed(date: LocalDateTime, bids: List<Bid>, basePrice: Int): AuctionStatus {
        throw IllegalStateException("Can't close a cancelled auction.")
    }

    override fun cancel(date: LocalDateTime): AuctionStatus {
        throw IllegalStateException("Can't cancel a cancelled auction.")
    }
}

sealed class Closed : AuctionStatus("CLOSED") {
    override fun closed(date: LocalDateTime, bids: List<Bid>, basePrice: Int): AuctionStatus {
        throw IllegalStateException("Can't close a closed auction.")
    }

    override fun cancel(date: LocalDateTime): AuctionStatus {
        throw IllegalStateException("Can't close a closed auction.")
    }
}

data class ClosedWithWinner(
    val closedOn: LocalDateTime,
    val winner: Buyer,
    val losers: List<Buyer>,
    val finalPrice: Int
) : Closed() {
    override fun toDTO(): StatusDTO = ClosedWithWinnerDTO(
        closedOn,
        winner.id,
        losers.map { it.id },
        finalPrice
    )
}

data class ClosedUnresolved(val closedOn: LocalDateTime) : Closed() {
    override fun toDTO(): StatusDTO = ClosedUnresolvedDTO(closedOn)
}

data class Bid(
    val offer: Int,
    val buyer: Buyer
)