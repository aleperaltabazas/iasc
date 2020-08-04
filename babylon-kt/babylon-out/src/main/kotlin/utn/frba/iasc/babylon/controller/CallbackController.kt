package utn.frba.iasc.babylon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.dto.NewAuctionCallbackDTO
import utn.frba.iasc.babylon.service.CallbackService

class CallbackController(
    private val objectMapper: ObjectMapper,
    private val callbackService: CallbackService
) : Controller {
    override fun register() {
        Spark.post("/babylon-out/callback", "application/json", this::closeCallback, objectMapper::writeValueAsString)
    }

    private fun closeCallback(req: Request, res: Response) {
        val newCallback: NewAuctionCallbackDTO = objectMapper.readValue(req.body())
        callbackService.scheduleClose(newCallback.auctionId, newCallback.timeout)
        callbackService.informInterested(newCallback.tags, newCallback.auctionId)
    }
}