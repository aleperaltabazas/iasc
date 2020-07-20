package utn.frba.iasc.service

import akka.actor.ActorRef
import utn.frba.iasc.actors.CreateUser
import utn.frba.iasc.dto.BuyerDTO
import utn.frba.iasc.model.Buyer

class UserService(
  private val usersActor: ActorRef
) {
  def register(buyerDTO: BuyerDTO, buyerId: String) {
    val buyer = Buyer(
      id = buyerId,
      username = buyerDTO.username,
      ip = buyerDTO.ip,
      interestTags = buyerDTO.interestTags
    )

    usersActor ! CreateUser(buyer)
  }
}
