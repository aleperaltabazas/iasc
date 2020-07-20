package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.util.Timeout
import utn.frba.iasc.actors.{CancelAuction, CreateAuction}
import utn.frba.iasc.dto.AuctionDTO
import utn.frba.iasc.model.{Auction, Open}

import scala.concurrent.duration._
import scala.language.postfixOps

class AuctionService(
  private val auctionActor: ActorRef
) {
  private implicit val findTimeout: Timeout = Timeout(5 seconds)

  def register(auctionDTO: AuctionDTO, id: String) {
    val auction = Auction(
      id = id,
      article = auctionDTO.article,
      status = Open,
      basePrice = auctionDTO.basePrice
    )

    auctionActor ! CreateAuction(auction, auctionDTO.maxDuration)
  }

  def cancel(id: String): Unit = {
    auctionActor ! CancelAuction(id)
  }
}
