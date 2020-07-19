package utn.frba.iasc.service

import akka.actor.ActorRef
import utn.frba.iasc.actors.CreateAuction
import utn.frba.iasc.dto.AuctionDTO
import utn.frba.iasc.model.{Auction, Open}

class AuctionService(
  private val auctionActor: ActorRef
) {

  def register(auctionDTO: AuctionDTO, id: String) {
    val auction = Auction(
      id = id,
      article = auctionDTO.article,
      status = Open,
      basePrice = auctionDTO.basePrice
    )

    auctionActor ! CreateAuction(auction, auctionDTO.maxDuration)
  }
}
