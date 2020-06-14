package utn.frba.iasc.db

import utn.frba.iasc.model.Buyer

class UserRepository(
  private var buyers: List[Buyer] = List()
) extends Repository[Buyer](buyers)
