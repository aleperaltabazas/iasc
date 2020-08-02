package utn.frba.iasc.actors

import akka.actor.{Actor, Cancellable}
import utn.frba.iasc.db.{AuctionRepository, BidRepository, JobsRepository, UserRepository}
import utn.frba.iasc.model.{Auction, Bid, Buyer}

class DatabaseNodeActor(
  private val userRepository: UserRepository,
  private val auctionRepository: AuctionRepository,
  private val bidRepository: BidRepository,
  private val jobsRepository: JobsRepository
) extends Actor {
  override def receive: Receive = process

  private def process: Receive = {
    case Create(a: Auction) => auctionRepository.add(a)
    case Create(b: Bid) => bidRepository.add(b)
    case Create(b: Buyer) => userRepository.add(b)
    case Create((auctionId: String, job: Cancellable)) => jobsRepository.add(auctionId, job)
    case Read(id: String, Auctions) => context.sender().tell(auctionRepository.find(id), self)
    case Read(id: String, Users) => context.sender().tell(userRepository.find(id), self)
    case Read(id: String, Bids) => context.sender().tell(bidRepository.find(id), self)
    case Update(a: Auction) => auctionRepository.update(a)
    case Update(b: Bid) => bidRepository.update(b)
    case Update(b: Buyer) => userRepository.update(b)
    case Delete(a: Auction) => auctionRepository.remove(a)
    case Delete(f: (Auction => Boolean)) => auctionRepository.removeBy(f)
    case Delete(b: Bid) => bidRepository.remove(b)
    case Delete(f: (Bid => Boolean)) => bidRepository.removeBy(f)
    case Delete(b: Buyer) => userRepository.remove(b)
    case Delete(f: (Buyer => Boolean)) => userRepository.removeBy(f)
    case Delete(id: String) => jobsRepository.cancel(id)
    case Combine(op1, op2) =>
      process(op1)
      process(op2)
  }
}
