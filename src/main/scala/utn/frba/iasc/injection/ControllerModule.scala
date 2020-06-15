package utn.frba.iasc.injection

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.controller.{AuctionController, BuyersController}
import utn.frba.iasc.service.{AuctionService, BidService, UserService}

object ControllerModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionController")
  def auctionController(
    @Named("auctionService") auctionService: AuctionService,
    @Named("bidService") bidService: BidService,
    @Named("objectMapper") objectMapper: ObjectMapper
  ): AuctionController = new AuctionController(
    auctionService = auctionService,
    bidService = bidService,
    mapper = objectMapper
  )

  @Provides
  @Singleton
  @Named("userController")
  def buyersController(
    @Named("userService") userService: UserService,
    @Named("objectMapper") objectMapper: ObjectMapper
  ): BuyersController = new BuyersController(
    userService = userService,
    mapper = objectMapper
  )
}
