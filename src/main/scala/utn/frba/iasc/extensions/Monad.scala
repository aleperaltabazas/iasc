package utn.frba.iasc.extensions

case object Monad {
  implicit class RichEither[A, B](e: Either[A, B]) {
    def getOrThrow: B = e.fold(_ => throw new RuntimeException(""), it => it)
  }
}
