package utn.frba.iasc.injection

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}

case object ActorsModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("actorSystem")
  def actorSystem(): ActorSystem = ActorSystem()

  @Provides
  @Singleton
  @Named("materializer")
  def actorMaterializer(
    @Named("actorSystem") actorSystem: ActorSystem
  ): ActorMaterializer = ActorMaterializer()(actorSystem)
}
