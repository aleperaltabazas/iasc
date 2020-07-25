package utn.frba.iasc.dto

import java.time.LocalDateTime

case class BuyerDTO(
  username: String,
  ip: String,
  interestTags: List[String]
)

case class CreateAuctionDTO(
  tags: List[String],
  basePrice: Int = 0,
  maxDuration: Int,
  article: String,
  seller: String
)

case class PlaceBidDTO(
  buyer: String,
  offer: Int
)

case class UserCreatedDTO(userId: String)

case class AuctionCreatedDTO(auctionId: String)

case class PlacedBidDTO(bidId: String)

case class BidDTO(
  user: String,
  offer: Int
)

case class AuctionDTO(
  status: AuctionStatusDTO,
  article: String,
  bids: List[BidDTO]
)

sealed trait AuctionStatusDTO

case object OpenDTO extends AuctionStatusDTO
case class CancelledDTO(on: LocalDateTime) extends AuctionStatusDTO
case class ClosedDTO(
  on: LocalDateTime,
  finalPrice: Option[Int],
  winner: Option[String]
) extends AuctionStatusDTO

sealed trait PerUserResultDTO

case object Winner extends PerUserResultDTO
case object Loser extends PerUserResultDTO
case object Cancelled extends PerUserResultDTO

case class CallbackDTO(
  auction: String,
  result: PerUserResultDTO
)
