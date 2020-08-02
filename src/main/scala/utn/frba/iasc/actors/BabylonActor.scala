package utn.frba.iasc.actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.model.{Auction, Buyer}

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

  override def receive: Receive = {
    case c@CreateAuction(_, _) => auctionActor ! c
    case c@CloseAuction(_) => auctionActor ! c
    case p@PlaceBid(_, _) => auctionActor ! p
    case c@CancelAuction(_) => auctionActor ! c
    case f@FindAuction(_) => auctionActor.ask(f)
      .mapTo[Auction]
      .foreach(a => context.sender().tell(a, self))
    case c@CreateUser(_) => usersActor ! c
    case f@FindAllSuchThat(_) => usersActor.ask(f)
      .mapTo[List[Auction]]
      .foreach(as => context.sender().tell(as, self))
    case f@FindFirstByUsername(_) => usersActor.ask(f)
      .mapTo[Buyer]
      .foreach(b => context.sender().tell(b, self))
  }
}
