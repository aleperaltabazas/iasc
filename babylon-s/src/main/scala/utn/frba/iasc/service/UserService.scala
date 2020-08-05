package utn.frba.iasc.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import utn.frba.iasc.actors.{CreateUser, FindFirstByUsername}
import utn.frba.iasc.dto.BuyerDTO
import utn.frba.iasc.exception.UsernameAlreadyExistsException
import utn.frba.iasc.model.Buyer

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class UserService(
  private val routerActor: ActorRef,
  private implicit val executionContext: ExecutionContext
) {
  private implicit val findTimeout: Timeout = 5 seconds

  def register(buyerDTO: BuyerDTO, buyerId: String): Future[Unit] = {
    routerActor.ask(FindFirstByUsername(buyerDTO.username))
      .mapTo[Option[Buyer]]
      .map {
        case Some(_) => throw new UsernameAlreadyExistsException(buyerDTO.username)
        case None =>
          val buyer = Buyer(
            id = buyerId,
            username = buyerDTO.username,
            ip = buyerDTO.ip,
            interestTags = buyerDTO.interestTags
          )

          routerActor ! CreateUser(buyer)
      }
  }
}
