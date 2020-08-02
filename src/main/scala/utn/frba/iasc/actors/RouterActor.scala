package utn.frba.iasc.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.slf4j.LoggerFactory
import utn.frba.iasc.extensions.Kotlin

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class RouterActor(
  private val system: ActorSystem,
  private implicit val executionContext: ExecutionContext,
  private val usersActor: ActorRef,
  private val auctionActor: ActorRef
) extends Actor with Kotlin {
  private implicit val timeout: Timeout = Timeout(5 seconds)
  private val babylons: mutable.Seq[ActorRef] = mutable.Seq(
    spawnBabylon,
    spawnBabylon,
    spawnBabylon
  )

  override def receive: Receive = {
    case c@CreateAuction(auction, _) => select(auction.id) ! c
    case c@CloseAuction(auctionId) => select(auctionId) ! c
    case p@PlaceBid(_, auctionId) => select(auctionId) ! p
    case c@CancelAuction(auctionId) => select(auctionId) ! c
    case f@FindAuction(auctionId) => select(auctionId).ask(f) pipeTo context.sender()
    case c@CreateUser(u) => select(u.id) ! c
    case f@FindAllSuchThat(_) => babylons.head.ask(f) pipeTo context.sender()
    case f@FindFirstByUsername(username) => select(username).ask(f) pipeTo context.sender()
  }

  private def select(str: String): ActorRef = {
    val last = str.last.toInt % babylons.length
    babylons(last)
  }

  private def spawnBabylon: ActorRef = system.actorOf(
    Props(
      new BabylonActor(auctionActor, usersActor, system, executionContext)
    )
  )

  private val LOGGER = LoggerFactory.getLogger(classOf[RouterActor])
}
