package utn.frba.iasc.service

import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}
import utn.frba.iasc.dto.BidDTO
import utn.frba.iasc.model.Bid
import utn.frba.iasc.utils.IdGen

class BidService(
  private val bidRepository: BidRepository,
  private val userRepository: UserRepository,
  private val auctionRepository: AuctionRepository,
  private val idGen: IdGen
) {
  def register(bidDTO: BidDTO, auctionId: String): String = {
    val id = idGen.bid
    val bid = Bid(
      id = id,
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
    id
  }
}
