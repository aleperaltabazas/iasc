package utn.frba.iasc.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.model.Buyer

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class CallbackActor(
  private val usersActor: ActorRef,
  private implicit val executionContext: ExecutionContext
) extends Actor {
  private implicit val retrieveUsersTimeout: Timeout = Timeout(10 seconds)

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[CallbackActor])

  override def receive: Receive = {
    case CallbackTo(endpoint, auctionId, result) =>
      LOGGER.info(s"Notifying to $endpoint of $result about $auctionId")
    case InformNewAuction(id, price, tags) =>
      usersActor.ask(FindAllSuchThat(b => b.interestTags.intersect(tags).nonEmpty))
        .mapTo[List[Buyer]]
        .onComplete {
          case Failure(e) => LOGGER.error(s"Error retrieving users interested in $tags", e)
          case Success(users) => users.foreach(u => LOGGER.info(s"Hey, ${u.username}! Check this auction: $id valued " +
            s"at $price"))
        }
  }
}
