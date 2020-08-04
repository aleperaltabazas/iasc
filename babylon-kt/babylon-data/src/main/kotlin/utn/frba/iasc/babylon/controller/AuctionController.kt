package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.service.AuctionService

class AuctionController(
    private val objectMapper: ObjectMapper,
    private val auctionService: AuctionService
) : Controller {
    override fun register() {
        Spark.post("/babylon-data/auctions", "application/json", this::createAuction, objectMapper::writeValueAsString)
        Spark.put("/babylon-data/auctions/:id/bids", "application/json", this::placeBid, objectMapper::writeValueAsString)
    }

    private fun createAuction(req: Request, res: Response) {
        val createAuction: CreateAuctionDTO = objectMapper.readValue(req.body())
        auctionService.create(createAuction)
    }

    private fun placeBid(req: Request, res: Response) {
        val placeBid: PlaceBidDTO = objectMapper.readValue(req.body())
        auctionService.placeBid(placeBid, req.params(":id"))
    }
}