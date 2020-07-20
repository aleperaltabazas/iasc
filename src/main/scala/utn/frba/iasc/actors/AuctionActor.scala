package utn.frba.iasc.actors

import akka.actor.{Actor, ActorLogging, ActorSystem}
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.db.{AuctionRepository, BidRepository, JobsRepository}
import utn.frba.iasc.model.{Auction, ClosedUnresolved, ClosedWithWinner}

import scala.concurrent.duration._
import scala.language.postfixOps

class AuctionActor(
  private val auctionRepository: AuctionRepository,
  private val bidRepository: BidRepository,
  private val jobsRepository: JobsRepository,
  private val system: ActorSystem
) extends Actor with ActorLogging {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionActor])

  import system.dispatcher

  override def receive: Receive = {
    case CreateAuction(auction, timeout) =>
      val cancelRef = system.scheduler.scheduleOnce(timeout seconds, self, CloseAuction(auction.id))
      LOGGER.info(s"Scheduled auction ${auction.id} to expire in $timeout seconds")
      auctionRepository.add(auction)
      jobsRepository.add(auction.id, cancelRef)
    case CloseAuction(auctionId) =>
      val auction = auctionRepository.find(auctionId).getOrElse {
        throw new NoSuchElementException("No auction found wth ID $auctionId")
      }

      val closedAuction = auction.closed()

      closedAuction.status match {
        case ClosedWithWinner(_, winner, finalPrice) =>
          LOGGER.info(s"Buyer ${winner.username} won with bid of $finalPrice")
        case ClosedUnresolved(_) => LOGGER.info(s"No winner for auction ${auction.id}")
      }

      auctionRepository.update(closedAuction)
    case PlaceBid(bid, auctionId) =>
      val auction = auctionRepository.find(auctionId).getOrElse {
        throw new NoSuchElementException(s"No auction found with ID $auctionId")
      }

      if (!auction.isOpen) {
        LOGGER.warn(s"Auction $auctionId is already closed; bid was not placed")
      }

      bidRepository.add(bid)
      auctionRepository.update(auction.addBid(bid))
    case CancelAuction(id) =>
      val auction = auctionRepository.find(id).getOrElse {
        throw new NoSuchElementException(s"No action found with ID $id")
      }

      val cancelledAuction = auction.cancelled()

      LOGGER.info(s"Cancelling auction $id")
      auctionRepository.update(cancelledAuction)
      jobsRepository.cancel(id)
    case FindAuction(id) => auctionRepository.find(id)
  }

  def find(id: String): Option[Auction] = auctionRepository.find(id)
}
