package utn.frba.iasc.db

import utn.frba.iasc.model.OngoingAuction

class OngoingAuctionRepository(
  private var ongoingAuctions: List[OngoingAuction] = List()
) extends Repository[OngoingAuction](ongoingAuctions)
