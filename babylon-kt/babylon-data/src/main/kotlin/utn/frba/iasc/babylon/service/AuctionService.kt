package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.exception.NotFoundException
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Bid
import utn.frba.iasc.babylon.model.Open
import utn.frba.iasc.babylon.storage.AuctionStorage
import utn.frba.iasc.babylon.storage.BuyerStorage
import utn.frba.iasc.babylon.util.IdGen

class AuctionService(
    private val auctionStorage: AuctionStorage,
    private val buyerStorage: BuyerStorage
) {
    fun create(createAuction: CreateAuctionDTO) {
        val auction = Auction(
            id = createAuction.auctionId,
            article = createAuction.article,
            status = Open,
            basePrice = createAuction.basePrice,
            bids = emptyList(),
            seller = createAuction.seller
        )

        auctionStorage.add(auction)
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        val buyer = buyerStorage.find { it.username == placeBid.buyer }
            ?: throw NotFoundException("User ${placeBid.buyer} not found")
        val auction = auctionStorage.find(auctionId)
            ?: throw NotFoundException("Auction $auctionId not found")
        val bid = Bid(
            offer = placeBid.offer,
            buyer = buyer
        )

        auctionStorage.update(auction.placeBid(bid))
    }
}