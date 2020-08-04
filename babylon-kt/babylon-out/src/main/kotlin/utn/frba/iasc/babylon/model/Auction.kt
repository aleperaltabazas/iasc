package utn.frba.iasc.babylon.model

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
    fun canBid(): Boolean = this == Open

    abstract fun closed(date: LocalDateTime, bids: List<Bid>, basePrice: Int): AuctionStatus

    abstract fun cancel(date: LocalDateTime): AuctionStatus

}

object Open : AuctionStatus("OPEN") {
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
) : Closed()

data class ClosedUnresolved(val closedOn: LocalDateTime) : Closed()

data class Bid(
    val offer: Int,
    val buyer: Buyer
)