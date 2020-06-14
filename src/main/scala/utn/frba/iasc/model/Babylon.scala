package utn.frba.iasc.model

import java.time.LocalDateTime

case class OngoingAuction(
  id: String,
  expirationDate: LocalDateTime,
  currentPrice: Int,
  currentWinner: Option[Buyer]
) extends Entity {
  def closed: ClosedAuction = ClosedAuction(
    id = String,
    closedOn = expirationDate,
    closingStatus = Expired,
    winner = currentWinner.map(b => Winner(currentPrice, b))
  )

  def cancelled: ClosedAuction = ClosedAuction(
    id = id,
    closedOn = LocalDateTime.now(),
    closingStatus = Cancelled,
    winner = None
  )
}

case class ClosedAuction(
  id: String,
  closedOn: LocalDateTime,
  closingStatus: ClosingStatus,
  winner: Option[Winner]
) extends Entity

case class Winner(
  finalPrice: Int,
  buyer: Buyer
)

sealed trait ClosingStatus

case object Cancelled extends ClosingStatus
case object Expired extends ClosingStatus

case class Buyer(
  id: String,
  username: String,
  ip: String,
  interestTags: List[String]
) extends Entity
