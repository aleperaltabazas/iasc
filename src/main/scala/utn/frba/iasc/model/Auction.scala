package utn.frba.iasc.model

import java.time.LocalDateTime

import utn.frba.iasc.dto._

case class Auction(
  id: String,
  article: String,
  status: AuctionStatus,
  basePrice: Int,
  bids: List[Bid] = List()
) extends Entity {
  def isOpen: Boolean = status == Open

  def closed(date: LocalDateTime = LocalDateTime.now()): Auction = copy(status = status.close(date, bids, basePrice))

  def cancelled(date: LocalDateTime = LocalDateTime.now()): Auction = copy(status = status.cancel(date))

  def addBid(bid: Bid): Auction = if (status.canBid) copy(bids = bid :: bids) else
    throw new IllegalStateException("Can't bid on a closed or cancelled auction.")

  def toDTO: AuctionDTO = AuctionDTO(
    article = article,
    bids = bids.map(_.toDTO),
    status = status.toDTO
  )
}

sealed abstract class AuctionStatus(val status: String) {
  def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus

  def cancel(date: LocalDateTime): AuctionStatus

  def canBid: Boolean

  def toDTO: AuctionStatusDTO
}

case object Open extends AuctionStatus("OPEN") {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = {
    bids.filter(_.offer >= basePrice).maxByOption(_.offer) match {
      case Some(winner) => ClosedWithWinner(
        date,
        finalPrice = winner.offer,
        winner = winner.buyer
      )
      case None => ClosedUnresolved(date)
    }
  }

  override def cancel(date: LocalDateTime): AuctionStatus = Cancelled(date)

  override def canBid: Boolean = true

  override def toDTO: AuctionStatusDTO = OpenDTO
}

case class Cancelled(cancelledOn: LocalDateTime) extends AuctionStatus("CANCELLED") {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = throw new
      IllegalStateException("Can't close a cancelled auction.")

  override def cancel(date: LocalDateTime): AuctionStatus = throw new IllegalStateException(
    "Can't cancel a cancelled auction."
  )

  override def canBid: Boolean = false

  override def toDTO: AuctionStatusDTO = CancelledDTO(cancelledOn)
}

sealed trait Closed extends AuctionStatus {
  override def close(date: LocalDateTime, bids: List[Bid], basePrice: Int): AuctionStatus = throw new
      IllegalStateException("Can't close a closed auction.")

  override def cancel(date: LocalDateTime): AuctionStatus = throw new IllegalStateException(
    "Can't close a closed auction."
  )

  override def canBid: Boolean = false
}

case class ClosedWithWinner(
  closedOn: LocalDateTime,
  winner: Buyer,
  finalPrice: Int
) extends AuctionStatus("WINNER") with Closed {
  override def toDTO: AuctionStatusDTO = ClosedDTO(closedOn, Some(finalPrice), winner = Some(winner.username))
}

case class ClosedUnresolved(closedOn: LocalDateTime) extends AuctionStatus("UNRESOLVED") with Closed {
  override def toDTO: AuctionStatusDTO = ClosedDTO(closedOn, None, None)
}
