package utn.frba.iasc.dto

case class BuyerDTO(
  username: String,
  ip: String,
  interestTags: List[String]
)

case class AuctionDTO(
  tags: List[String],
  basePrice: Int = 0,
  maxDuration: Int, //in seconds
  article: String
)

case class BidDTO(
  username: String,
  offer: Int
)
