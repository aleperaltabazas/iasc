package utn.frba.iasc.actors

import utn.frba.iasc.model.{Auction, Bid, Buyer}

case class AddAuction(auction: Auction)

case class AddBid(bid: Bid, auction: Auction)

case class AddBuyer(buyer: Buyer)

case class UpdateAuction(auction: Auction)