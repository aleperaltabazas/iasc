package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Open
import utn.frba.iasc.babylon.storage.AuctionStorage
import utn.frba.iasc.babylon.util.IdGen

class AuctionService(
    private val auctionStorage: AuctionStorage
) {
    fun create(createAuction: CreateAuctionDTO) {
        val auction = Auction(
            id = IdGen.auction(),
            article = createAuction.article,
            status = Open,
            basePrice = createAuction.basePrice,
            bids = emptyList(),
            seller = createAuction.seller
        )

        auctionStorage.add(auction)
    }
}