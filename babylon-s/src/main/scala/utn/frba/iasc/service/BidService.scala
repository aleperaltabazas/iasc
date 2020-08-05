package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.actors.{FindFirstByUsername, PlaceBid}
import utn.frba.iasc.dto.PlaceBidDTO
import utn.frba.iasc.exception.NotFoundException
import utn.frba.iasc.model.{Bid, Buyer}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class BidService(
  private val routerActor: ActorRef,
  private implicit val executionContext: ExecutionContext
) {
  private implicit val findTimeout: Timeout = 10 seconds

  def place(bidDTO: PlaceBidDTO, auctionId: String, bidId: String): Future[Unit] = {
    routerActor.ask(FindFirstByUsername(bidDTO.buyer)).mapTo[Option[Buyer]]
      .map(maybeBuyer => maybeBuyer.getOrElse(throw new NotFoundException(s"User ${bidDTO.buyer} not found")))
      .map {
        b =>
          val bid = Bid(
            id = bidId,
            bidDTO.offer,
            buyer = b
          )
          routerActor ! PlaceBid(bid, auctionId)
      }
  }
}
