package utn.frba.iasc.service

import akka.actor.ActorRef
import utn.frba.iasc.actors.PlaceBid
import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}
import utn.frba.iasc.dto.PlaceBidDTO
import utn.frba.iasc.model.Bid

class BidService(
  private val bidRepository: BidRepository,
  private val userRepository: UserRepository,
  private val auctionRepository: AuctionRepository,
  private val auctionActor: ActorRef
) {
  def place(bidDTO: PlaceBidDTO, auctionId: String, bidId: String) {
    val bid = Bid(
      id = bidId,
      bidDTO.offer,
      buyer = userRepository.find(_.username == bidDTO.buyer).getOrElse {
        throw new NoSuchElementException(s"No buyer found with ID ${bidDTO.buyer}")
      }
    )

    auctionActor ! PlaceBid(bid, auctionId)
  }
}
