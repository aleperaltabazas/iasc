package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.AuctionCreatedDTO
import utn.frba.iasc.babylon.dto.InCreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.service.AuctionService

class AuctionController(
    private val objectMapper: ObjectMapper,
    private val auctionService: AuctionService
) : Controller {
    override fun register() {
        Spark.post("/babylon-in/auctions", "application/json", this::createAuction, objectMapper::writeValueAsString)
        Spark.put("/babylon-in/auctions/:id/bids", "application/json", this::placeBid, objectMapper::writeValueAsString)
        Spark.post("/babylon-in/auctions/:id/cancel", "application/json", this::cancel, objectMapper::writeValueAsString)
    }

    private fun createAuction(req: Request, res: Response): AuctionCreatedDTO {
        val createAuction: InCreateAuctionDTO = objectMapper.readValue(req.body())
        val id = auctionService.create(createAuction)

        return AuctionCreatedDTO(id)
    }

    private fun placeBid(req: Request, res: Response) {
        val placeBid: PlaceBidDTO = objectMapper.readValue(req.body())
        auctionService.placeBid(placeBid, req.params(":id"))
    }

    private fun cancel(req: Request, res: Response) {
        auctionService.cancel(req.params(":id"))
    }
}