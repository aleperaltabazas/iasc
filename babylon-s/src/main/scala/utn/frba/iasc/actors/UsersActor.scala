package utn.frba.iasc.actors

import akka.actor.{Actor, ActorLogging}
import org.slf4j.LoggerFactory
import utn.frba.iasc.db.UserRepository
import utn.frba.iasc.model.Buyer

object UsersActor {
  case class CreateUser(buyer: Buyer)
}

class UsersActor(
  private val userRepository: UserRepository
) extends Actor with ActorLogging {
  private val LOGGER = LoggerFactory.getLogger(classOf[UsersActor])

  override def receive: Receive = {
    case CreateUser(buyer) =>
      LOGGER.info(s"Create user ${buyer.username}")
      userRepository.add(buyer)
    case FindAllSuchThat(f) =>
      LOGGER.info("Finding users matching condition")
      val users = userRepository.findAll(f)
      context.sender().tell(users, self)
    case FindFirstByUsername(username) =>
      LOGGER.info(s"${context.sender()}")
      LOGGER.info(s"Find user $username")
      context.sender().tell(userRepository.find(_.username == username), self)
  }
}
