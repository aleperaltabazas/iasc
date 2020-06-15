package utn.frba.iasc.controller

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import spark.{Request, Response, Spark}
import utn.frba.iasc.dto.{AuctionDTO, BidDTO, CreatedAuctionDTO, PlacedBidDTO}
import utn.frba.iasc.service.{AuctionService, BidService}

class AuctionController(
  private val auctionService: AuctionService,
  private val bidService: BidService,
  private val objectMapper: ObjectMapper
) extends Controller {

  def register(): Unit = {
    Spark.post("/auctions", (req: Request, res: Response) => bid(req, res))
    Spark.post("/auctions/:auction/bids", (req: Request, res: Response) => bid(req, res))
  }

  def bid(req: Request, res: Response): PlacedBidDTO = {
    val bidDTO = objectMapper.readValue(req.body(), new TypeReference[BidDTO] {})
    val auctionId = req.params(":auction")
    PlacedBidDTO(bidService.register(bidDTO, auctionId))
  }

  def createAuction(req: Request, res: Response): CreatedAuctionDTO = {
    val auctionDTO: AuctionDTO = objectMapper.readValue(req.body(), new TypeReference[AuctionDTO] {})
    CreatedAuctionDTO(auctionService.register(auctionDTO))
  }
}
