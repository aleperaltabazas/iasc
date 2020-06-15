package utn.frba.iasc.injection


import java.text.SimpleDateFormat

import com.google.inject.name.Named
import com.google.inject.{Provides, Singleton}
import utn.frba.iasc.utils.{Clock, IdGen}

object UtilsModule {
  @Provides
  @Singleton
  @Named("clock")
  def clock: Clock = new Clock()

  @Provides
  @Singleton
  @Named("idGen")
  def idGen: IdGen = new IdGen(new SimpleDateFormat("yyyyMMddHHmm"))
}
