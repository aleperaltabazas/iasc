package utn.frba.iasc.model

import utn.frba.iasc.dto.BidDTO

case class Buyer(
  id: String,
  username: String,
  ip: String,
  interestTags: List[String]
) extends Entity

case class Bid(id: String, offer: Int, buyer: Buyer) extends Entity {
  def toDTO: BidDTO = BidDTO(user = buyer.username, offer = offer)
}
