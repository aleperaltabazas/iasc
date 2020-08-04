package utn.frba.iasc.babylon.config

import akka.actor.ActorRef
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.CallbackService

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("callbackService")
    fun callbackService(
        @Named("callbackActor") callbackActor: ActorRef
    ) = CallbackService(callbackActor)
}