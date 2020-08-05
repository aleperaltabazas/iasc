package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.actors.{CancelAuction, CreateAuction, FindAuction, FindFirstByUsername}
import utn.frba.iasc.dto.CreateAuctionDTO
import utn.frba.iasc.exception.NotFoundException
import utn.frba.iasc.model.{Auction, Buyer, Open}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class AuctionService(
  private val routerActor: ActorRef,
  private implicit val executionContext: ExecutionContext
) {
  private implicit val findTimeout: Timeout = Timeout(5 seconds)

  def register(auctionDTO: CreateAuctionDTO, id: String): Future[Unit] = {
    routerActor.ask(FindFirstByUsername(auctionDTO.seller))
      .mapTo[Option[Buyer]]
      .map {
        case None => throw new NotFoundException(s"User ${auctionDTO.seller} not found")
        case Some(_) =>
          val auction = Auction(
            id = id,
            article = auctionDTO.article,
            status = Open,
            basePrice = auctionDTO.basePrice,
            seller = auctionDTO.seller
          )

          routerActor ! CreateAuction(auction, auctionDTO.maxDuration)
      }
  }

  def cancel(id: String): Unit = routerActor ! CancelAuction(id)

  def find(id: String): Future[Auction] = routerActor.ask(FindAuction(id))
    .mapTo[Option[Auction]]
    .map {
      case Some(a) => a
      case None => throw new NotFoundException(s"Auction $id not found")
    }
}
