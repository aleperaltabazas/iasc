package utn.frba.iasc.model

import java.util.Date

case class OngoingAuction(
  expirationDate: Date,
  currentPrice: Int,
  currentWinner: Option[Buyer]
)

case class ClosedAuction(
  closedOn: Date,
  finalPrice: Int,
  winner: Buyer
)

case class Buyer(
  username: String,
  ip: String,
  interestTags: List[String]
)
