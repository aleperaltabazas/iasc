package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.service.DataService

class DataController(
    private val objectMapper: ObjectMapper,
    private val dataService: DataService
) : Controller {
    override fun register() {
        Spark.post("/babylon-storage/auctions", "application/json", this::createAuction,
            objectMapper::writeValueAsString)
        Spark.put("/babylon-storage/auctions/:id/bids", "application/json", this::placeBid,
            objectMapper::writeValueAsString)
    }

    private fun createAuction(req: Request, res: Response) {
        val createAuction: CreateAuctionDTO = objectMapper.readValue(req.body())
        return dataService.createAuction(createAuction)
    }

    private fun placeBid(req: Request, res: Response) {
        val placeBid: PlaceBidDTO = objectMapper.readValue(req.body())
        dataService.placeBid(placeBid, req.params(":id"))
    }
}