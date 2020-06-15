package utn.frba.iasc.db

import utn.frba.iasc.model.Auction

class AuctionRepository(
  private var auctions: List[Auction] = List()
) extends Repository(auctions)
