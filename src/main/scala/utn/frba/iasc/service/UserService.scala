package utn.frba.iasc.service

import utn.frba.iasc.db.UserRepository
import utn.frba.iasc.dto.BuyerDTO
import utn.frba.iasc.model.Buyer
import utn.frba.iasc.utils.IdGen

class UserService(
  private val userRepository: UserRepository,
  private val idGen: IdGen
) {
  def register(buyer: BuyerDTO): String = {
    val id = idGen.user
    userRepository.register {
      Buyer(
        id = id,
        username = buyer.username,
        ip = buyer.ip,
        interestTags = buyer.interestTags
      )
    }
    id
  }
}
