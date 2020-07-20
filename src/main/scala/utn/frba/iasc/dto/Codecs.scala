package utn.frba.iasc.dto

import io.circe.Encoder
import io.circe._
import io.circe.generic.semiauto._
import utn.frba.iasc.model.{Auction, Bid}

trait BuyerCodec {
  implicit val buyerEncoder: Encoder[BuyerDTO] = deriveEncoder[BuyerDTO]
  implicit val buyerDecoder: Decoder[BuyerDTO] = deriveDecoder[BuyerDTO]
}

trait AuctionCodec {
  implicit val auctionEncoder: Encoder[AuctionDTO] = deriveEncoder[AuctionDTO]
  implicit val auctionDecoder: Decoder[AuctionDTO] = deriveDecoder[AuctionDTO]
}

trait BidCodec {
  implicit val bidEncoder: Encoder[BidDTO] = deriveEncoder[BidDTO]
  implicit val bidDecoder: Decoder[BidDTO] = deriveDecoder[BidDTO]
}

trait CreatedAuctionCodec {
  implicit val createdAuctionEncoder: Encoder[CreatedAuctionDTO] = deriveEncoder[CreatedAuctionDTO]
  implicit val createdAuctionDecoder: Decoder[CreatedAuctionDTO] = deriveDecoder[CreatedAuctionDTO]
}

trait BidPlacedCodec {
  implicit val bidPlacedEncoder: Encoder[PlacedBidDTO] = deriveEncoder[PlacedBidDTO]
  implicit val bidPlacedDecoder: Decoder[PlacedBidDTO] = deriveDecoder[PlacedBidDTO]
}

trait UserCodec {
  implicit val userCreatedEncoder: Encoder[UserCreatedDTO] = deriveEncoder[UserCreatedDTO]
  implicit val userCreatedDecoder: Decoder[UserCreatedDTO] = deriveDecoder[UserCreatedDTO]
}

trait AuctionFindCodec {
  implicit val auctionFindEncoder: Encoder[Auction] = deriveEncoder[Auction]
  implicit val auctionFindDecoder: Decoder[Auction] = deriveDecoder[Auction]
  implicit val bidEncoder: Encoder[Bid] = deriveEncoder[Bid]
  implicit val bidDecoder: Decoder[Bid] = deriveDecoder[Bid]
}
