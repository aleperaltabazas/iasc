package utn.frba.iasc.service

import utn.frba.iasc.db.AuctionRepository
import utn.frba.iasc.dto.AuctionDTO
import utn.frba.iasc.model.{Auction, Open}
import utn.frba.iasc.utils.{Clock, IdGen}

class AuctionService(
  private val auctionRepository: AuctionRepository,
  private val idGen: IdGen,
  private val clock: Clock
) {
  def register(auctionDTO: AuctionDTO): Unit = {
    val expirationDate = clock.now.plusSeconds(auctionDTO.maxDuration)
    val auction = Auction(
      id = idGen.auction,
      article = auctionDTO.article,
      status = Open(expirationDate = expirationDate),
      basePrice = auctionDTO.basePrice
    )

    auctionRepository.register(auction)
  }
}
