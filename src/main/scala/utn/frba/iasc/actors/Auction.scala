package utn.frba.iasc.actors

import utn.frba.iasc.dto.AuctionDTO

sealed trait AuctionMessage

case class CreateAuction(auctionDTO: AuctionDTO) extends AuctionMessage
case object PlaceBid extends AuctionMessage
