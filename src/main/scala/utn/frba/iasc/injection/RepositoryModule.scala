package utn.frba.iasc.injection

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides, Singleton}
import utn.frba.iasc.db.{AuctionRepository, BidRepository, UserRepository}

object RepositoryModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("auctionRepository")
  def auctionRepository: AuctionRepository = new AuctionRepository()

  @Provides
  @Singleton
  @Named("userRepository")
  def userRepository: UserRepository = new UserRepository()

  @Provides
  @Singleton
  @Named("bidRepository")
  def bidRepository: BidRepository = new BidRepository()

}
