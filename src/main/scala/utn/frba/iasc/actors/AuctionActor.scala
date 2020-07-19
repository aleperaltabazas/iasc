package utn.frba.iasc.actors

import akka.actor.{Actor, ActorLogging, ActorSystem}
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.db.AuctionRepository
import utn.frba.iasc.model.{ClosedUnresolved, ClosedWithWinner}

import scala.concurrent.duration._
import scala.language.postfixOps

class AuctionActor(
  private val auctionRepository: AuctionRepository,
  private val system: ActorSystem
) extends Actor with ActorLogging {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionActor])

  import system.dispatcher

  override def receive: Receive = {
    case CreateAuction(auction, timeout) =>
      auctionRepository.add(auction)
      system.scheduler.scheduleOnce(timeout seconds, self, CloseAuction(auction))
      LOGGER.info(s"Scheduled auction ${auction.id} to expire in $timeout seconds")
    case CloseAuction(auction) =>
      val closedAuction = auction.closed()

      closedAuction.status match {
        case ClosedWithWinner(_, winner, finalPrice) =>
          LOGGER.info(s"Buyer ${winner.username} won with bid of $finalPrice")
        case ClosedUnresolved(_) => LOGGER.info(s"No winner for auction ${auction.id}")
      }

      auctionRepository.update(closedAuction)
  }
}
