package utn.frba.iasc.model

import java.time.LocalDateTime

case class Auction(
  id: String,
  article: String,
  status: AuctionStatus,
  basePrice: Int,
  bids: List[Bid] = List()
) extends Entity {
  def close(date: LocalDateTime): Auction = copy(status = status.close(date, bids, basePrice))

  def cancel(date: LocalDateTime): Auction = copy(status = status.cancel(date))
}

sealed abstract class AuctionStatus(val status: String) {
  def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus

  def cancel(date: LocalDateTime): AuctionStatus
}

case class Open(expirationDate: LocalDateTime) extends AuctionStatus("OPEN") {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = {
    bids.filter(_.offer >= basePrice).maxByOption(_.offer) match {
      case Some(winner) => ClosedWithWinner(
        expirationDate,
        finalPrice = winner.offer,
        winner = winner.buyer
      )
      case None => ClosedUnresolved(expirationDate)
    }
  }

  override def cancel(date: LocalDateTime): AuctionStatus = Cancelled(date)
}

case class Cancelled(cancelledOn: LocalDateTime) extends AuctionStatus("CANCELLED") {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = throw new
      IllegalStateException("Can't close a cancelled auction.")

  override def cancel(date: LocalDateTime): AuctionStatus = throw new IllegalStateException(
    "Can't cancel a cancelled auction."
  )
}

sealed trait Closed extends AuctionStatus {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = throw new
      IllegalStateException("Can't close a closed auction.")

  override def cancel(date: LocalDateTime): AuctionStatus = throw new IllegalStateException(
    "Can't close a closed auction."
  )
}

case class ClosedWithWinner(
  closedOn: LocalDateTime,
  winner: Buyer,
  finalPrice: Int
) extends AuctionStatus("WINNER") with Closed

case class ClosedUnresolved(closedOn: LocalDateTime) extends AuctionStatus("UNRESOLVED") with Closed
