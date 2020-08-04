package utn.frba.iasc.babylon.service

import akka.actor.ActorRef
import utn.frba.iasc.babylon.actor.PlaceBid
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO


class DataService(
    private val replicaSet: ActorRef
) {
    fun createAuction(createAuction: CreateAuctionDTO) {
        replicaSet.tell(createAuction, ActorRef.noSender())
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        replicaSet.tell(PlaceBid(placeBid, auctionId), ActorRef.noSender())
    }

    fun createBuyer(createBuyer: CreateBuyerDTO) {
        replicaSet.tell(createBuyer, ActorRef.noSender())
    }
}