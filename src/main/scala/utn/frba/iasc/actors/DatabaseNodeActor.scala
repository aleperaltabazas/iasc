package utn.frba.iasc.actors

import akka.actor.{Actor, ActorLogging}
import utn.frba.iasc.extensions.CollectionsSyntax
import utn.frba.iasc.model.{Auction, Bid, Buyer}

import scala.collection.mutable

class DatabaseNodeActor(
  private val buyers: mutable.Buffer[Buyer] = mutable.Buffer.empty,
  private val auctions: mutable.Buffer[Auction] = mutable.Buffer.empty,
  private val bids: mutable.Buffer[Bid] = mutable.Buffer.empty
) extends Actor with ActorLogging with CollectionsSyntax {
  override def receive: Receive = {
    case AddAuction(a) => auctions.addOne(a)
    case AddBid(bid, auction) =>
      auctions.removeBy(a => a.id == auction.id)
      bids.addOne(bid)
    case AddBuyer(buyer) => buyers.addOne(buyer)
    case UpdateAuction(auction) =>
      auctions.removeBy(a => a.id == auction.id)
      auctions.addOne(auction)
  }
}
