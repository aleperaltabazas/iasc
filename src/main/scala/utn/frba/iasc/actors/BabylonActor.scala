package utn.frba.iasc.actors

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class BabylonActor(
  private val auctionActor: ActorRef,
  private val usersActor: ActorRef,
  private val system: ActorSystem,
  private implicit val executionContext: ExecutionContext
) extends Actor {
  private implicit val timeout: Timeout = Timeout(5 seconds)
  private val LOGGER = LoggerFactory.getLogger(classOf[BabylonActor])

  override def receive: Receive = {
    case c@CreateAuction(_, _) => auctionActor ! c
    case c@CloseAuction(_) => auctionActor ! c
    case p@PlaceBid(_, _) => auctionActor ! p
    case c@CancelAuction(_) => auctionActor ! c
    case f@FindAuction(_) => auctionActor.ask(f) pipeTo context.sender()
    case c@CreateUser(_) => usersActor ! c
    case f@FindAllSuchThat(_) => usersActor.ask(f) pipeTo context.sender()
    case f@FindFirstByUsername(_) => usersActor.ask(f) pipeTo context.sender
  }
}
