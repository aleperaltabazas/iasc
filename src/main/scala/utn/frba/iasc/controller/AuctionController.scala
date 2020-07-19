package utn.frba.iasc.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, path, post, _}
import akka.http.scaladsl.server.Route
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.dto.{AuctionCodec, AuctionDTO, BidCodec, BidDTO}
import utn.frba.iasc.extensions.{CirceExtensionSyntax, Kotlin}
import utn.frba.iasc.service.{AuctionService, BidService}
import io.circe.parser._

class AuctionController(
  private val auctionService: AuctionService,
  private val bidService: BidService,
) extends Controller
  with Kotlin
  with CirceExtensionSyntax
  with AuctionCodec
  with BidCodec {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[AuctionController])

  override def routes: Route = concat(
    (path("auctions" / Segment / "bids") & post) { id: String =>
      entity(as[String]) { body: String =>
        val bid = decode[BidDTO](body)
        LOGGER.info(s"Create new bid on auction $id")
        complete((StatusCodes.OK, ""))
      }
    },
    (path("auctions") & post) {
      entity(as[String]) { body: String =>
        val auction = decodeOrThrow[AuctionDTO](body)
        LOGGER.info("Create new auction")
        complete((StatusCodes.OK, ""))
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
