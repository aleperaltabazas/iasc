package utn.frba.iasc.injection

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.actors.AuctionActor
import utn.frba.iasc.db.{AuctionRepository, BidRepository}

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
    @Named("bidRepository") bidRepository: BidRepository
  ): ActorRef = system.actorOf(Props(new AuctionActor(auctionRepository, bidRepository, system)), "auctionActor")
}
