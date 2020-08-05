package utn.frba.iasc.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Cancellable}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.dto.{Loser, Winner}
import utn.frba.iasc.model.{Auction, ClosedUnresolved, ClosedWithWinner}

import scala.concurrent.duration._
import scala.language.postfixOps

class AuctionActor(
  private val callbackActor: ActorRef,
  private val databaseActor: ActorRef,
  private val system: ActorSystem
) extends Actor with ActorLogging {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionActor])
  private implicit val timeout: Timeout = Timeout(5 seconds)

  import system.dispatcher

  override def receive: Receive = {
    case CreateAuction(auction, timeout) =>
      val cancelRef: Cancellable = system.scheduler.scheduleOnce(timeout seconds, self, CloseAuction(auction.id))
      LOGGER.info(s"Scheduled auction ${auction.id} to expire in $timeout seconds")
      databaseActor ! Create(auction)
      databaseActor ! Create((auction.id, cancelRef))
    case CloseAuction(auctionId) =>
      databaseActor.ask(Read(auctionId, Auctions))
        .mapTo[Option[Auction]]
        .foreach {
          case None => throw new NoSuchElementException("No auction found wth ID $auctionId")
          case Some(auction) =>
            val closedAuction = auction.closed()

            closedAuction.status match {
              case ClosedWithWinner(_, winner, losers, finalPrice) =>
                LOGGER.info(s"Buyer ${winner.username} won with an offer of $finalPrice buck-os")
                callbackActor ! CallbackTo(endpoint = winner.ip, auction = auctionId, result = Winner)
                losers.foreach { l =>
                  callbackActor ! CallbackTo(endpoint = l.ip, auction = auctionId, result = Loser)
                }
              case ClosedUnresolved(_) => LOGGER.info(s"No winner for auction ${auction.id}")
            }

            databaseActor ! Update(closedAuction)
        }

    case PlaceBid(bid, auctionId) =>
      databaseActor.ask(Read(auctionId, Auctions))
        .mapTo[Option[Auction]]
        .foreach {
          case None => throw new NoSuchElementException(s"No auction found with ID $auctionId")
          case Some(auction) =>
            if (!auction.isOpen) {
              LOGGER.warn(s"Auction $auctionId is already closed; bid was not placed")
            }

            databaseActor ! Combine(Create(bid), Update(auction.addBid(bid)))
        }
    case CancelAuction(id) =>
      databaseActor.ask(Read(id, Auctions))
        .mapTo[Option[Auction]]
        .foreach {
          case None => throw new NoSuchElementException(s"No auction found with ID $id")
          case Some(auction) =>
            val cancelledAuction = auction.cancelled()
            LOGGER.info(s"Cancelling auction $id")
            databaseActor ! Combine(Update(cancelledAuction), Delete(id))
        }


    case FindAuction(id) =>
      LOGGER.info(s"Search auction $id")
      databaseActor.ask(Read(id, Auctions)) pipeTo context.sender()
  }
}
