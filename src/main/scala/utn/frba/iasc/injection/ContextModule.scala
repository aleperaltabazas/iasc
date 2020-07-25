package utn.frba.iasc.injection

import akka.actor.ActorSystem
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}

import scala.concurrent.ExecutionContextExecutor

object ContextModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("executionContext")
  def executionContext(
    @Named("actorSystem") system: ActorSystem
  ): ExecutionContextExecutor = system.dispatcher
}
