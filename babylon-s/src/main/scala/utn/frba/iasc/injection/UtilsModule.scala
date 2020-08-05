package utn.frba.iasc.injection


import java.text.SimpleDateFormat

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.utils.{Clock, IdGen}

object UtilsModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("clock")
  def clock: Clock = new Clock()

  @Provides
  @Singleton
  @Named("idGen")
  def idGen: IdGen = new IdGen(new SimpleDateFormat("yyyyMMddHHmm"))
}
