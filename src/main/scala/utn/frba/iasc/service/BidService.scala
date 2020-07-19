package utn.frba.iasc.service

import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}
import utn.frba.iasc.dto.BidDTO
import utn.frba.iasc.model.Bid

class BidService(
  private val bidRepository: BidRepository,
  private val userRepository: UserRepository,
  private val auctionRepository: AuctionRepository
) {
  def register(bidDTO: BidDTO, auctionId: String, bidId: String) {
    val bid = Bid(
      id = bidId,
      bidDTO.offer,
      buyer = userRepository.find(bidDTO.buyerId).getOrElse {
        throw new NoSuchElementException(s"No buyer found with ID ${bidDTO.buyerId}")
      }
    )

    val auction = auctionRepository.find(auctionId)
      .map(_.addBid(bid))
      .getOrElse {
        throw new NoSuchElementException(s"No auction found with ID ${bidDTO.buyerId}")
      }

    bidRepository.register(bid)
    auctionRepository.update(auction)
  }
}
