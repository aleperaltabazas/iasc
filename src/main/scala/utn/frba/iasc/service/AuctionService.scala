package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.actors.{CancelAuction, CreateAuction, FindAuction}
import utn.frba.iasc.dto.CreateAuctionDTO
import utn.frba.iasc.model.{Auction, Open}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

class AuctionService(
  private val auctionActor: ActorRef
) {
  private implicit val findTimeout: Timeout = Timeout(5 seconds)

  def register(auctionDTO: CreateAuctionDTO, id: String) {
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

  def find(id: String): Future[Option[Auction]] = ask(auctionActor, FindAuction(id)).mapTo[Option[Auction]]
}
