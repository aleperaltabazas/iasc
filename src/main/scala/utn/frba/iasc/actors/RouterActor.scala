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

import akka.cluster.sharding._

object RouterActor {
}

class RouterActor(
  private val system: ActorSystem,
  private implicit val executionContext: ExecutionContext,
  private val usersActor: ActorRef,
  private val auctionActor: ActorRef
) extends Actor with Kotlin {
  private implicit val timeout: Timeout = Timeout(5 seconds)
//  private val babylons: mutable.Seq[ActorRef] = mutable.Seq(
//    spawnBabylon,
//    spawnBabylon,
//    spawnBabylon
//  )


  private val extractEntityId: ShardRegion.ExtractEntityId = {
    case c@CreateAuction(auction, _) => (shard(auction.id), c)
    case c@CloseAuction(auctionId) => (shard(auctionId), c)
    case p@PlaceBid(_, auctionId) => (shard(auctionId), p)
    case c@CancelAuction(auctionId) => (shard(auctionId), c)
    case f@FindAuction(auctionId) => (shard(auctionId), f)
    case c@CreateUser(u) => (shard(u.id), c)
    case f@FindAllSuchThat(_) => ("1", f)
    case f@FindFirstByUsername(username) => (shard(username), f)
  }

  private val numberOfShards = 3

  private val extractShardId: ShardRegion.ExtractShardId = {
//    case Device.RecordTemperature(id, _) => (id % numberOfShards).toString
    // Needed if you want to use 'remember entities':
    //case ShardRegion.StartEntity(id) => (id.toLong % numberOfShards).toString

    case CreateAuction(auction, _) => shard(auction.id)
    case CloseAuction(auctionId) => shard(auctionId)
    case PlaceBid(_, auctionId) => shard(auctionId)
    case CancelAuction(auctionId) => shard(auctionId)
    case FindAuction(auctionId) => shard(auctionId)
    case CreateUser(u) => shard(u.id)
    case FindAllSuchThat(_) => "1"
    case FindFirstByUsername(username) => shard(username)
  }

  val babylonRegion: ActorRef = ClusterSharding(context.system).start(
    typeName = "BabylonActor",
    entityProps = Props(
      new BabylonActor(auctionActor, usersActor, system, executionContext)
    ),
    settings = ClusterShardingSettings(context.system),
    extractEntityId = extractEntityId,
    extractShardId = extractShardId)

//  implicit val ec: ExecutionContext = context.dispatcher


  override def receive: Receive = {
    case msg => babylonRegion ! msg
//    case c@CreateAuction(auction, _) => select(auction.id) ! c
//    case c@CloseAuction(auctionId) => select(auctionId) ! c
//    case p@PlaceBid(_, auctionId) => select(auctionId) ! p
//    case c@CancelAuction(auctionId) => select(auctionId) ! c
//    case f@FindAuction(auctionId) => select(auctionId).ask(f) pipeTo context.sender()
//    case c@CreateUser(u) => select(u.id) ! c
//    case f@FindAllSuchThat(_) => babylons.head.ask(f) pipeTo context.sender()
//    case f@FindFirstByUsername(username) => select(username).ask(f) pipeTo context.sender()
  }

//  private def select(str: String): ActorRef = {
//    val last = str.last.toInt % babylons.length
//    babylons(last)
//  }

  private def shard(str: String): String = {
    val last = str.last.toInt % numberOfShards
    last.toString
  }

//  private def spawnBabylon: ActorRef = system.actorOf(
//    Props(
//      new BabylonActor(auctionActor, usersActor, system, executionContext)
//    )
//  )

  private val LOGGER = LoggerFactory.getLogger(classOf[RouterActor])
}
