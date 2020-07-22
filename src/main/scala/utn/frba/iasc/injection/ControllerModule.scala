package utn.frba.iasc.injection

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.controller.{AuctionCreatedController, BuyersController}
import utn.frba.iasc.service.{AuctionService, BidService, UserService}
import utn.frba.iasc.utils.IdGen

object ControllerModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionController")
  def auctionController(
    @Named("auctionService") auctionService: AuctionService,
    @Named("bidService") bidService: BidService,
    @Named("idGen") idGen: IdGen
  ): AuctionCreatedController = new AuctionCreatedController(
    auctionService = auctionService,
    bidService = bidService,
    idGen = idGen
  )

  @Provides
  @Singleton
  @Named("userController")
  def buyersController(
    @Named("userService") userService: UserService,
    @Named("idGen") idGen: IdGen
  ): BuyersController = new BuyersController(
    userService = userService,
    idGen = idGen
  )
}
