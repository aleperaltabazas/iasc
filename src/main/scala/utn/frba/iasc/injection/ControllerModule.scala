package utn.frba.iasc.injection

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
  ): AuctionController = new AuctionController(
    auctionService = auctionService,
    bidService = bidService,
  )

  @Provides
  @Singleton
  @Named("userController")
  def buyersController(
    @Named("userService") userService: UserService,
  ): BuyersController = new BuyersController(
    userService = userService,
  )
}
