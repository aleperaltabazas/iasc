package utn.frba.iasc.injection

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}
import utn.frba.iasc.service.{AuctionService, BidService, UserService}
import utn.frba.iasc.utils.{Clock, IdGen}

object ServiceModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionService")
  def auctionService(
    @Named("auctionRepository") auctionRepository: AuctionRepository,
    @Named("clock") clock: Clock,
    @Named("idGen") idGen: IdGen
  ): AuctionService = new AuctionService(
    auctionRepository = auctionRepository,
    clock = clock,
    idGen = idGen
  )

  @Provides
  @Singleton
  @Named("userService")
  def userService(
    @Named("userRepository") userRepository: UserRepository,
    @Named("clock") clock: Clock,
    @Named("idGen") idGen: IdGen
  ): UserService = new UserService(
    userRepository = userRepository,
    idGen = idGen
  )

  @Provides
  @Singleton
  @Named("bidService")
  def bidService(
    @Named("bidRepository") bidRepository: BidRepository,
    @Named("userRepository") userRepository: UserRepository,
    @Named("auctionRepository") auctionRepository: AuctionRepository,
    @Named("idGen") idGen: IdGen
  ): BidService = new BidService(
    bidRepository = bidRepository,
    userRepository = userRepository,
    auctionRepository = auctionRepository,
    idGen = idGen
  )
}
