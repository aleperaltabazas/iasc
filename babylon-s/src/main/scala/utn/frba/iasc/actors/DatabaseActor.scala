package utn.frba.iasc.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import utn.frba.iasc.db.{AuctionRepository, BidRepository, JobsRepository, UserRepository}

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class DatabaseActor(
  private val userRepository: UserRepository,
  private val auctionRepository: AuctionRepository,
  private val bidRepository: BidRepository,
  private val jobsRepository: JobsRepository,
  private val system: ActorSystem,
  private implicit val executionContext: ExecutionContext
) extends Actor {
  private implicit val timeout: Timeout = Timeout(5 seconds)
  private val nodes: mutable.Seq[ActorRef] = mutable.Seq(
    spawnNode,
    spawnNode,
    spawnNode
  )

  override def receive: Receive = {
    case c: Create[_] => nodes.foreach { n => n ! c }
    case r: Read[_] => nodes.head.ask(r) pipeTo context.sender()
    case u: Update[_] => nodes.foreach { n => n ! u }
    case d: Delete[_] => nodes.foreach { n => n ! d }
    case combinedOp: Combine[_, _] => nodes.foreach { n => n ! combinedOp }
  }

  private def spawnNode: ActorRef = system.actorOf(
    Props(
      new DatabaseNodeActor(userRepository, auctionRepository, bidRepository, jobsRepository)
    )
  )
}
