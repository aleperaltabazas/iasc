package utn.frba.iasc.actors

import akka.actor.Actor
import org.slf4j.{Logger, LoggerFactory}

class CallbackActor extends Actor {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[CallbackActor])

  override def receive: Receive = {
    case CallbackTo(endpoint, auctionId, result) =>
      LOGGER.info(s"Notifying to $endpoint of $result about $auctionId")
  }
}
