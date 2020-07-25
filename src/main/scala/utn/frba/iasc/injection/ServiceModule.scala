package utn.frba.iasc.injection

import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}
import utn.frba.iasc.service.{AuctionService, BidService, UserService}

object ServiceModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionService")
  def auctionService(
    @Named("auctionActorRef") auctionActor: ActorRef
  ): AuctionService = new AuctionService(
    auctionActor = auctionActor
  )

  @Provides
  @Singleton
  @Named("userService")
  def userService(
    @Named("usersActorRef") usersActor: ActorRef,
    @Named("actorSystem") system: ActorSystem
  ): UserService = new UserService(
    usersActor = usersActor,
    executionContext = system.dispatcher
  )

  @Provides
  @Singleton
  @Named("bidService")
  def bidService(
    @Named("bidRepository") bidRepository: BidRepository,
    @Named("userRepository") userRepository: UserRepository,
    @Named("auctionRepository") auctionRepository: AuctionRepository,
    @Named("auctionActorRef") auctionActorRef: ActorRef
  ): BidService = new BidService(
    bidRepository = bidRepository,
    userRepository = userRepository,
    auctionRepository = auctionRepository,
    auctionActor = auctionActorRef
  )
}
