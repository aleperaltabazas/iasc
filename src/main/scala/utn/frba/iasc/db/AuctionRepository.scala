package utn.frba.iasc.db

import utn.frba.iasc.model.{ClosedAuction, OngoingAuction}

class AuctionRepository(
  private var ongoingAuctions: List[OngoingAuction],
  private var closedAuctions: List[ClosedAuction]
) {

}
