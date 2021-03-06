package utn.frba.iasc.actors

import utn.frba.iasc.dto.PerUserResultDTO

case class CallbackTo(
  endpoint: String,
  auction: String,
  result: PerUserResultDTO
)

case class InformNewAuction(
  auctionId: String,
  startingPrice: Int,
  tags: List[String]
)
