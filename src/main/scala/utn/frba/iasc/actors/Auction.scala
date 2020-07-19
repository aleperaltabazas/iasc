package utn.frba.iasc.actors

import utn.frba.iasc.model.Auction

case class CreateAuction(auction: Auction, timeout: Int)
case class CloseAuction(auction: Auction)
case object PlaceBid
