package utn.frba.iasc.actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.extensions.Kotlin
import utn.frba.iasc.model.{Auction, Buyer}

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class RouterActor(
  private val system: ActorSystem,
  private implicit val executionContext: ExecutionContext
) extends Actor with Kotlin {
  private implicit val timeout: Timeout = Timeout(5 seconds)
  private val babylons: mutable.Seq[ActorRef] = mutable.Seq.empty

  override def receive: Receive = {
    case c@CreateAuction(auction, _) => select(auction.id) ! c
    case c@CloseAuction(auctionId) => select(auctionId) ! c
    case p@PlaceBid(_, auctionId) => select(auctionId) ! p
    case c@CancelAuction(auctionId) => select(auctionId) ! c
    case f@FindAuction(auctionId) => select(auctionId).ask(f)
      .mapTo[Auction]
      .foreach(a => context.sender().tell(a, self))
    case c@CreateUser(u) => select(u.id) ! c
    case f@FindAllSuchThat(_) => babylons.head.ask(f)
      .mapTo[List[Auction]]
      .foreach(as => context.sender().tell(as, self))
    case f@FindFirstByUsername(username) => select(username).ask(f)
      .mapTo[Buyer]
      .foreach(b => context.sender().tell(b, self))
  }

  private def select(str: String): ActorRef = str.last.let { it => babylons(it.toInt % babylons.length) }
}
