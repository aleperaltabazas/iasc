package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.CloseAuctionDTO
import utn.frba.iasc.babylon.service.CallbackService

class CallbackController(
    private val objectMapper: ObjectMapper,
    private val callbackService: CallbackService
) : Controller {
    override fun register() {
        Spark.post("/babylon-out/close-callback", "application/json", this::closeCallback, objectMapper::writeValueAsString)
    }

    private fun closeCallback(req: Request, res: Response) {
        val close: CloseAuctionDTO = objectMapper.readValue(req.body())
        callbackService.scheduleClose(close.auctionId, close.timeout)
    }
}