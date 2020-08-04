package utn.frba.iasc.babylon.service

import akka.actor.ActorRef
import utn.frba.iasc.babylon.actor.CloseIn
import utn.frba.iasc.babylon.actor.Inform

class CallbackService(
    private val callbackActor: ActorRef
) {
    fun scheduleClose(id: String, timeout: Int) {
        callbackActor.tell(CloseIn(id, timeout), ActorRef.noSender())
    }

    fun informInterested(tags: List<String>, auctionId: String) {
        callbackActor.tell(Inform(tags, auctionId), ActorRef.noSender())
    }
}