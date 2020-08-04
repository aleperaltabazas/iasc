package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.dto.*
import utn.frba.iasc.babylon.exception.NotFoundException
import utn.frba.iasc.babylon.model.*
import utn.frba.iasc.babylon.storage.AuctionStorage
import utn.frba.iasc.babylon.storage.BuyerStorage

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

    fun listAuctions(): List<Auction> = auctionStorage.findAll()

    fun updateStatus(updateStatus: UpdateStatusDTO, auctionId: String) {
        val status = when (updateStatus) {
            is CancelledDTO -> Cancelled(updateStatus.cancelledOn)
            is ClosedWithWinnerDTO -> ClosedWithWinner(
                closedOn = updateStatus.closedOn,
                finalPrice = updateStatus.finalPrice,
                losers = updateStatus.losersId.map { buyerStorage.findOrThrow(it) },
                winner = buyerStorage.findOrThrow(updateStatus.winnerId)
            )
            is ClosedUnresolvedDTO -> ClosedUnresolved(updateStatus.closedOn)
        }

        val auction = auctionStorage.findOrThrow(auctionId)
        auctionStorage.update(auction.copy(status = status))
    }

    fun find(id: String): Auction = auctionStorage.find(id) ?: throw NotFoundException("Auction $id not found")
}