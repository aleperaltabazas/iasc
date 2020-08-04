package utn.frba.iasc.babylon.service

import akka.actor.ActorRef
import akka.pattern.Patterns
import utn.frba.iasc.babylon.actor.*
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.UpdateStatusDTO
import utn.frba.iasc.babylon.model.Auction
import utn.frba.iasc.babylon.model.Buyer
import java.time.Duration
import java.util.concurrent.TimeUnit


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

    fun updateStatus(updateStatus: UpdateStatusDTO, id: String){
        replicaSet.tell(UpdateStatus(updateStatus, id), ActorRef.noSender())
    }

    fun find(id: String): Auction = Patterns.ask(replicaSet, Any, Duration.ofSeconds(2)).toCompletableFuture()
        .get(2, TimeUnit.SECONDS)
        .let { result ->
            when (result) {
                is Client -> result.client.find(id)
                else -> throw RuntimeException("Failed to get neighbour hosts")
            }
        }

    fun buyersInterestedIn(tags: String?): List<Buyer> = Patterns.ask(
        replicaSet, Any, Duration.ofSeconds(2))
        .toCompletableFuture()
        .get(2, TimeUnit.SECONDS)
        .let { result ->
            when (result) {
                is Client -> result.client.buyers(tags)
                else -> throw RuntimeException("Failed to get neighbour hosts")
            }
        }
}