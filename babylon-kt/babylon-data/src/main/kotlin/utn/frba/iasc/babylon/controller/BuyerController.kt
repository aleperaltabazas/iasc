package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.service.BuyerService

class BuyerController(
    private val objectMapper: ObjectMapper,
    private val buyerService: BuyerService
) : Controller {
    override fun register() {
        Spark.post("/babylon-data/buyers", "application/json", this::createBuyer, objectMapper::writeValueAsString)
        Spark.get("/babylon-data/buyers", "application/json", this::listBuyers, objectMapper::writeValueAsString)
    }

    private fun createBuyer(req: Request, res: Response) {
        val createBuyer: CreateBuyerDTO = objectMapper.readValue(req.body())
        buyerService.createBuyer(createBuyer)
    }

    private fun listBuyers(req: Request, res: Response) = buyerService.listBuyers()

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BuyerController::class.java)
    }
}