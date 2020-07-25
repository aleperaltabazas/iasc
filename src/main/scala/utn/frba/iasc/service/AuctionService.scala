package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.actors.{CancelAuction, CreateAuction, FindAuction, FindFirstByUsername}
import utn.frba.iasc.dto.CreateAuctionDTO
import utn.frba.iasc.exception.NotFoundException
import utn.frba.iasc.model.{Auction, Open}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class AuctionService(
  private val auctionActor: ActorRef,
  private val usersActor: ActorRef,
  private implicit val executionContext: ExecutionContext
) {
  private implicit val findTimeout: Timeout = Timeout(5 seconds)

  def register(auctionDTO: CreateAuctionDTO, id: String): Future[Unit] = {
    usersActor.ask(FindFirstByUsername(auctionDTO.seller))
      .mapTo[Boolean]
      .map {
        case false => throw new NotFoundException(s"User ${auctionDTO.seller} not found")
        case true =>
          val auction = Auction(
            id = id,
            article = auctionDTO.article,
            status = Open,
            basePrice = auctionDTO.basePrice
          )

          auctionActor ! CreateAuction(auction, auctionDTO.maxDuration)
      }
  }

  def cancel(id: String): Unit = {
    auctionActor ! CancelAuction(id)
  }

  def find(id: String): Future[Option[Auction]] = ask(auctionActor, FindAuction(id)).mapTo[Option[Auction]]
}
