package utn.frba.iasc.injection

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.actors._
import utn.frba.iasc.db.{AuctionRepository, BidRepository, JobsRepository, UserRepository}

import scala.concurrent.ExecutionContextExecutor

case object ActorsModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("actorSystem")
  def actorSystem(): ActorSystem = ActorSystem()

  @Provides
  @Singleton
  @Named("materializer")
  def actorMaterializer(
    implicit @Named("actorSystem") actorSystem: ActorSystem
  ): ActorMaterializer = ActorMaterializer()

  @Provides
  @Singleton
  @Named("auctionActorRef")
  def auctionActorRef(
    @Named("actorSystem") system: ActorSystem,
    @Named("auctionRepository") auctionRepository: AuctionRepository,
    @Named("bidRepository") bidRepository: BidRepository,
    @Named("jobsRepository") jobsRepository: JobsRepository,
    @Named("callbackActorRef") callbackActor: ActorRef,
    @Named("databaseActorRef") databaseActor: ActorRef
  ): ActorRef = system.actorOf(
    Props(new AuctionActor(callbackActor, databaseActor, system)), "auctionActor"
  )

  @Provides
  @Singleton
  @Named("usersActorRef")
  def usersActorRef(
    @Named("actorSystem") system: ActorSystem,
    @Named("userRepository") userRepository: UserRepository
  ): ActorRef = system.actorOf(Props(new UsersActor(userRepository)), "usersActor")

  @Provides
  @Singleton
  @Named("callbackActorRef")
  def callbackActorRef(
    @Named("actorSystem") system: ActorSystem,
    @Named("usersActorRef") usersActor: ActorRef,
    @Named("executionContext") executionContext: ExecutionContextExecutor
  ): ActorRef = system.actorOf(Props(new CallbackActor(usersActor, executionContext)), "callbackActor")

  @Provides
  @Singleton
  @Named("routerActorRef")
  def routerActor(
    @Named("actorSystem") system: ActorSystem,
    @Named("executionContext") executionContextExecutor: ExecutionContextExecutor,
    @Named("usersActorRef") usersActor: ActorRef,
    @Named("auctionActorRef") auctionActor: ActorRef
  ): ActorRef = system.actorOf(
    Props(
      new RouterActor(
        system = system,
        executionContext = executionContextExecutor,
        usersActor = usersActor,
        auctionActor = auctionActor
      )
    ), "routerActor"
  )

  @Provides
  @Singleton
  @Named("databaseActorRef")
  def databaseActorRef(
    @Named("userRepository") userRepository: UserRepository,
    @Named("auctionRepository") auctionRepository: AuctionRepository,
    @Named("bidRepository") bidRepository: BidRepository,
    @Named("jobsRepository") jobsRepository: JobsRepository,
    @Named("actorSystem") system: ActorSystem,
    @Named("executionContext") executionContextExecutor: ExecutionContextExecutor
  ): ActorRef = system.actorOf(
    Props(
      new DatabaseActor(
        userRepository = userRepository,
        auctionRepository = auctionRepository,
        bidRepository = bidRepository,
        jobsRepository = jobsRepository,
        system = system,
        executionContext = executionContextExecutor
      )
    )
  )
}
