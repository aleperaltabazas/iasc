package utn.frba.iasc.actors

import utn.frba.iasc.model.Buyer

case class CreateUser(buyer: Buyer)

case class FindAllSuchThat(condition: Buyer => Boolean)

case class FindFirstByUsername(username: String)
