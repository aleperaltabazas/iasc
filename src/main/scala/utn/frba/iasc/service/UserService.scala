package utn.frba.iasc.service

import utn.frba.iasc.db.UserRepository
import utn.frba.iasc.dto.BuyerDTO
import utn.frba.iasc.model.Buyer

class UserService(
  private val userRepository: UserRepository,
) {
  def register(buyer: BuyerDTO, buyerId: String) {
    userRepository.register {
      Buyer(
        id = buyerId,
        username = buyer.username,
        ip = buyer.ip,
        interestTags = buyer.interestTags
      )
    }
  }
}
