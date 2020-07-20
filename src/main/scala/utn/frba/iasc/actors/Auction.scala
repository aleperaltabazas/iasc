package utn.frba.iasc.actors

import utn.frba.iasc.model.{Auction, Bid}

case class CreateAuction(auction: Auction, timeout: Int)
case class CloseAuction(auctionId: String)
case class PlaceBid(bid: Bid, auctionId: String)
case class CancelAuction(auctionId: String)