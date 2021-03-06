package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.UpdateStatusDTO
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Buyer
import utn.frba.iasc.babylon.service.DataService

class DataController(
    private val objectMapper: ObjectMapper,
    private val dataService: DataService
) : Controller {
    override fun register() {
        Spark.post(
            "/babylon-storage/auctions",
            "application/json",
            this::createAuction,
            objectMapper::writeValueAsString
        )
        Spark.put(
            "/babylon-storage/auctions/:id/bids",
            "application/json",
            this::placeBid,
            objectMapper::writeValueAsString
        )
        Spark.post(
            "/babylon-storage/buyers",
            "application/json",
            this::createBuyer,
            objectMapper::writeValueAsString
        )
        Spark.patch(
            "/babylon-storage/auctions/:id/status",
            "application/json",
            this::updateStatus,
            objectMapper::writeValueAsString
        )
        Spark.get(
            "/babylon-storage/auctions/:id",
            "application/json",
            this::find,
            objectMapper::writeValueAsString
        )
        Spark.get(
            "/babylon-storage/buyers",
            "application/json",
            this::buyers,
            objectMapper::writeValueAsString
        )
    }

    private fun createAuction(req: Request, res: Response) {
        val createAuction: CreateAuctionDTO = objectMapper.readValue(req.body())
        return dataService.createAuction(createAuction)
    }

    private fun placeBid(req: Request, res: Response) {
        val placeBid: PlaceBidDTO = objectMapper.readValue(req.body())
        dataService.placeBid(placeBid, req.params(":id"))
    }

    private fun createBuyer(req: Request, res: Response) {
        val createBuyer: CreateBuyerDTO = objectMapper.readValue(req.body())
        dataService.createBuyer(createBuyer)
    }

    private fun updateStatus(req: Request, res: Response) {
        val updateStatus: UpdateStatusDTO = objectMapper.readValue(req.body())
        dataService.updateStatus(updateStatus, req.params(":id"))
    }

    private fun find(req: Request, res: Response): Auction {
        val id = req.params(":id")
        return dataService.find(id)
    }

    private fun buyers(req: Request, res: Response): List<Buyer> = dataService.buyersInterestedIn(req.queryParams("tags"))
}