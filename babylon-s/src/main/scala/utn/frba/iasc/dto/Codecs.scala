package utn.frba.iasc.dto

import io.circe.generic.semiauto._
import io.circe.{Encoder, _}

trait BuyerCodec {
  implicit val buyerEncoder: Encoder[BuyerDTO] = deriveEncoder[BuyerDTO]
  implicit val buyerDecoder: Decoder[BuyerDTO] = deriveDecoder[BuyerDTO]
}

trait CreateAuctionCodec {
  implicit val createAuctionEncoder: Encoder[CreateAuctionDTO] = deriveEncoder[CreateAuctionDTO]
  implicit val createAuctionDecoder: Decoder[CreateAuctionDTO] = deriveDecoder[CreateAuctionDTO]
}

trait PlaceBidCodec {
  implicit val placeBidEncoder: Encoder[PlaceBidDTO] = deriveEncoder[PlaceBidDTO]
  implicit val placeBidDecoder: Decoder[PlaceBidDTO] = deriveDecoder[PlaceBidDTO]
}

trait AuctionCreatedCodec {
  implicit val auctionCreatedEncoder: Encoder[AuctionCreatedDTO] = deriveEncoder[AuctionCreatedDTO]
  implicit val auctionCreatedDecoder: Decoder[AuctionCreatedDTO] = deriveDecoder[AuctionCreatedDTO]
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
  implicit val auctionEncoder: Encoder[AuctionDTO] = deriveEncoder[AuctionDTO]
  implicit val auctionDecoder: Decoder[AuctionDTO] = deriveDecoder[AuctionDTO]
  implicit val bidEncoder: Encoder[BidDTO] = deriveEncoder[BidDTO]
  implicit val bidDecoder: Decoder[BidDTO] = deriveDecoder[BidDTO]
  implicit val openEncoder: Encoder[AuctionStatusDTO] = deriveEncoder[AuctionStatusDTO]
  implicit val openDecoder: Decoder[AuctionStatusDTO] = deriveDecoder[AuctionStatusDTO]
  implicit val cancelledEncoder: Encoder[CancelledDTO] = deriveEncoder[CancelledDTO]
  implicit val cancelledDecoder: Decoder[CancelledDTO] = deriveDecoder[CancelledDTO]
  implicit val closedEncoder: Encoder[ClosedDTO] = deriveEncoder[ClosedDTO]
  implicit val closedDecoder: Decoder[ClosedDTO] = deriveDecoder[ClosedDTO]
}
