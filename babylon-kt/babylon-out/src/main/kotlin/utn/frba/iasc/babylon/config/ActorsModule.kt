package utn.frba.iasc.babylon.config

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.actor.CallbackActor
import utn.frba.iasc.babylon.client.BabylonStorageClient

object ActorsModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("callbackActorProps")
    fun callbackActorProps(
        @Named("storageClient") babylonStorageClient: BabylonStorageClient
    ): Props = Props.create(CallbackActor::class.java) { CallbackActor(babylonStorageClient) }

    @Provides
    @Singleton
    @Named("callbackActor")
    fun replicaSet(
        @Named("callbackActorProps") props: Props,
        @Named("actorSystem") system: ActorSystem
    ): ActorRef = system.actorOf(props)

    @Provides
    @Singleton
    @Named("actorSystem")
    fun system(): ActorSystem = ActorSystem.create()
}