package utn.frba.iasc.dto

case class BuyerDTO(
  username: String,
  ip: String,
  interestTags: List[String]
)

case class AuctionDTO(
  tags: List[String],
  basePrice: Int = 0,
  maxDuration: Int,
  article: String
)

case class BidDTO(
  buyerId: String,
  offer: Int
)

case class CreatedAuctionDTO(auctionId: String)

case class PlacedBidDTO(bidId: String)
