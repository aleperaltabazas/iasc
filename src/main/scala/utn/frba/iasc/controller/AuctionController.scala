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

class AuctionController(
  private val auctionService: AuctionService,
  private val bidService: BidService,
  private val idGen: IdGen
) extends Controller with Kotlin with AuctionCodec with CreatedAuctionCodec with BidCodec with BidPlacedCodec {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionController])

  override def routes: Route = concat(
    (path("auctions" / Segment / "bids") & post) { auctionId: String =>
      entity(as[BidDTO]) { bid: BidDTO =>
        LOGGER.info(s"Create new bid on auction $auctionId")
        val bidId = idGen.bid
        bidService.place(bid, auctionId, bidId)
        complete(StatusCodes.Accepted, PlacedBidDTO(bidId))
      }
    },
    (path("auctions") & post) {
      entity(as[AuctionDTO]) { auction: AuctionDTO =>
        LOGGER.info("Create new auction")
        val id = idGen.auction
        auctionService.register(auction, id)
        complete(StatusCodes.Accepted, CreatedAuctionDTO(id))
      }
    },
  )

  //  def bid(req: Request, res: Response): PlacedBidDTO = {
  //    val bidDTO = decode[BidDTO](req.body()).getOrThrow
  //    val auctionId = req.params(":auction")
  //    PlacedBidDTO(bidService.register(bidDTO, auctionId))
  //  }
  //
  //  def createAuction(req: Request, res: Response): CreatedAuctionDTO = {
  //    val auctionDTO = decode[AuctionDTO](req.body()).getOrThrow
  //    CreatedAuctionDTO(auctionService.register(auctionDTO))
  //  }
}
