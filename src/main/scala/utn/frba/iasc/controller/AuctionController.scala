package utn.frba.iasc.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, path, post, _}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.dto._
import utn.frba.iasc.extensions.Kotlin
import utn.frba.iasc.service.{AuctionService, BidService}
import utn.frba.iasc.utils.IdGen

import scala.util.{Failure, Success}

class AuctionController(
  private val auctionService: AuctionService,
  private val bidService: BidService,
  private val idGen: IdGen
) extends Controller
  with Kotlin
  with CreateAuctionCodec
  with AuctionCreatedCodec
  with PlaceBidCodec
  with BidPlacedCodec
  with AuctionFindCodec {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionController])

  override def routes: Route = concat(
    (path("auctions" / Segment) & get) { auctionId: String =>
      onComplete(auctionService.find(auctionId)) {
        case Success(Some(a)) => complete(StatusCodes.OK, a.toDTO)
        case Success(None) => complete(StatusCodes.NotFound, s"Auction $auctionId not found")
        case Failure(_) => complete(StatusCodes.InternalServerError, s"Error searching $auctionId")
      }
    },
    (path("auctions" / Segment / "bids") & put) { auctionId: String =>
      entity(as[PlaceBidDTO]) { bid: PlaceBidDTO =>
        LOGGER.info(s"Create new bid on auction $auctionId")
        val bidId = idGen.bid
        bidService.place(bid, auctionId, bidId)
        complete(StatusCodes.Accepted, PlacedBidDTO(bidId))
      }
    },
    (path("auctions") & post) {
      entity(as[CreateAuctionDTO]) { auction: CreateAuctionDTO =>
        LOGGER.info("Create new auction")
        val id = idGen.auction
        onComplete(auctionService.register(auction, id)) {
          case Failure(exception) => handle(exception)
          case Success(_) => complete(StatusCodes.Accepted, AuctionCreatedDTO(id))
        }
      }
    },
    (path("auctions" / Segment) & delete) { auctionId: String =>
      LOGGER.info(s"Cancel bid $auctionId")
      auctionService.cancel(auctionId)
      complete(StatusCodes.OK)
    }
  )
}
