package utn.frba.iasc.babylon.config

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.storage.ReplicaSet

object ActorsModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("replicaSetProps")
    fun replicaSetProps(
        @Named("objectMapper") objectMapper: ObjectMapper
    ): Props = Props.create(ReplicaSet::class.java) { ReplicaSet(objectMapper) }

    @Provides
    @Singleton
    @Named("replicaSet")
    fun replicaSet(
        @Named("replicaSetProps") props: Props,
        @Named("actorSystem") system: ActorSystem
    ): ActorRef = system.actorOf(props)

    @Provides
    @Singleton
    @Named("actorSystem")
    fun system(): ActorSystem = ActorSystem.create()
}