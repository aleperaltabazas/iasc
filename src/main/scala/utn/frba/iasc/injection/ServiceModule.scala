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
    @Named("routerActorRef") routerActor: ActorRef,
    @Named("executionContext") executionContext: ExecutionContextExecutor
  ): AuctionService = new AuctionService(
    routerActor = routerActor,
    executionContext = executionContext
  )

  @Provides
  @Singleton
  @Named("userService")
  def userService(
    @Named("routerActorRef") routerActor: ActorRef,
    @Named("actorSystem") system: ActorSystem
  ): UserService = new UserService(
    routerActor = routerActor,
    executionContext = system.dispatcher
  )

  @Provides
  @Singleton
  @Named("bidService")
  def bidService(
    @Named("routerActorRef") routerActor: ActorRef,
    @Named("executionContext") executionContextExecutor: ExecutionContextExecutor
  ): BidService = new BidService(
    routerActor = routerActor,
    executionContext = executionContextExecutor
  )
}
