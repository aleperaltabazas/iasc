package utn.frba.iasc.service

import utn.frba.iasc.db.AuctionRepository
import utn.frba.iasc.dto.AuctionDTO
import utn.frba.iasc.model.{Auction, Open}
import utn.frba.iasc.utils.Clock

class AuctionService(
  private val auctionRepository: AuctionRepository,
  private val clock: Clock
) {

  def register(auctionDTO: AuctionDTO, id: String) {
    val expirationDate = clock.now.plusSeconds(auctionDTO.maxDuration)
    val auction = Auction(
      id = id,
      article = auctionDTO.article,
      status = Open(expirationDate = expirationDate),
      basePrice = auctionDTO.basePrice
    )

    auctionRepository.register(auction)
    // schedule expiration
  }
}
