package utn.frba.iasc.babylon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.controller.AuctionController
import utn.frba.iasc.babylon.controller.BuyerController
import utn.frba.iasc.babylon.service.AuctionService
import utn.frba.iasc.babylon.service.BuyerService

object ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("auctionController")
    fun auctionController(
        @Named("objectMapper") objectMapper: ObjectMapper,
        @Named("auctionService") auctionService: AuctionService
    ) = AuctionController(objectMapper, auctionService)

    @Provides
    @Singleton
    @Named("buyerController")
    fun buyerController(
        @Named("objectMapper") objectMapper: ObjectMapper,
        @Named("buyerService") buyerService: BuyerService
    ) = BuyerController(objectMapper, buyerService)
}