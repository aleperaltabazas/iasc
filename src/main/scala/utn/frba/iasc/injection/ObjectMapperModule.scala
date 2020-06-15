package utn.frba.iasc.injection

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, PropertyNamingStrategy}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.inject.name.Named
import com.google.inject.{Provides, Singleton}

object ObjectMapperModule {
  @Provides
  @Singleton
  @Named("objectMapper")
  def objectMapperSnakeCase: ObjectMapper = {
    val objectMapper = new ObjectMapper()
      .registerModule(DefaultScalaModule)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

    objectMapper
  }
}
