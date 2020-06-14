package utn.frba.iasc.db

import utn.frba.iasc.model.Bid

class BidRepository(
  private var bids: List[Bid] = List()
) extends Repository(bids)
