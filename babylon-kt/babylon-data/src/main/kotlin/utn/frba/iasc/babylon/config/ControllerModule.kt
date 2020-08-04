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
        @Named("auctionService") auctionService: AuctionService,
        @Named("objectMapper") objectMapper: ObjectMapper
    ) = AuctionController(objectMapper, auctionService)

    @Provides
    @Singleton
    @Named("buyerController")
    fun buyerController(
        @Named("buyerService") buyerService: BuyerService,
        @Named("objectMapper") objectMapper: ObjectMapper
    ) = BuyerController(objectMapper, buyerService)
}