package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ConfigModule : AbstractModule() {
    @Provides
    @Singleton
    fun config(): Config = ConfigFactory.load()
}