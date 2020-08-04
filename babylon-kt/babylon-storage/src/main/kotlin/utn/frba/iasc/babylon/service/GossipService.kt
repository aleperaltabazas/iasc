package utn.frba.iasc.babylon.service

import akka.actor.ActorRef
import akka.pattern.Patterns.ask
import utn.frba.iasc.babylon.actor.Add
import utn.frba.iasc.babylon.actor.All
import utn.frba.iasc.babylon.actor.Clients
import java.time.Duration
import java.util.concurrent.TimeUnit

class GossipService(
    private val replicaSet: ActorRef
) {
    fun dataNeighbourHosts(): List<String> = ask(replicaSet, All, Duration.ofSeconds(2)).toCompletableFuture()
        .get(2, TimeUnit.SECONDS)
        .let { result ->
            when (result) {
                is Clients -> result.clients.map { it.host() }
                else -> throw RuntimeException("Failed to get neighbour hosts")
            }
        }

    fun register(host: String) {
        replicaSet.tell(Add(host), ActorRef.noSender())
    }
}