package utn.frba.iasc.model

case class Buyer(
  id: String,
  username: String,
  ip: String,
  interestTags: List[String]
) extends Entity

case class Bid(offer: Int, buyer: Buyer)
