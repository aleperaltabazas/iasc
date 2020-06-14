package utn.frba.iasc.db

import utn.frba.iasc.model.ClosedAuction

class ClosedAuctionRepository(
  private var closedAuctions: List[ClosedAuction] = List()
) extends Repository[ClosedAuction](closedAuctions)