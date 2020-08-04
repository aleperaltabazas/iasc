package utn.frba.iasc.babylon.service

import akka.actor.ActorRef
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO


class DataService(
    private val replicaSet: ActorRef
) {
    fun createAuction(createAuction: CreateAuctionDTO) {
        replicaSet.tell(createAuction, ActorRef.noSender())
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        replicaSet.tell(Pair(placeBid, auctionId), ActorRef.noSender())
    }
}