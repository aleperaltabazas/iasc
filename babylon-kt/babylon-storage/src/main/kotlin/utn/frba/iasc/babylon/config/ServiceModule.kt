package utn.frba.iasc.babylon.config

import akka.actor.ActorRef
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.DataService
import utn.frba.iasc.babylon.service.GossipService

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("dataService")
    fun dataService(
        @Named("replicaSet") replicaSet: ActorRef
    ) = DataService(replicaSet)

    @Provides
    @Singleton
    @Named("gossipService")
    fun gossipService(
        @Named("replicaSet") replicaSet: ActorRef
    ) = GossipService(replicaSet)
}