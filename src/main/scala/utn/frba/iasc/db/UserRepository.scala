package utn.frba.iasc.db

import utn.frba.iasc.model.Buyer

class UserRepository(
  private var buyers: List[Buyer] = List()
) {
  def registerUser(buyer: Buyer): Unit = {
    buyers = buyer :: buyers
  }

  def get(username: String): Option[Buyer] = buyers.find(_.username == username)

  def getAll: List[Buyer] = buyers
}
