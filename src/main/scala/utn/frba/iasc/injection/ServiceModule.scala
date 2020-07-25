package utn.frba.iasc.injection

import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.service.{AuctionService, BidService, UserService}

import scala.concurrent.ExecutionContextExecutor

object ServiceModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionService")
  def auctionService(
    @Named("auctionActorRef") auctionActor: ActorRef,
    @Named("usersActorRef") usersActor: ActorRef,
    @Named("executionContext") executionContext: ExecutionContextExecutor
  ): AuctionService = new AuctionService(
    auctionActor = auctionActor,
    usersActor = usersActor,
    executionContext = executionContext
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
    @Named("auctionActorRef") auctionActorRef: ActorRef,
    @Named("usersActorRef") usersActor: ActorRef,
    @Named("executionContext") executionContextExecutor: ExecutionContextExecutor
  ): BidService = new BidService(
    auctionActor = auctionActorRef,
    usersActor = usersActor,
    executionContext = executionContextExecutor
  )
}
