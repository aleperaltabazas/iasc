package utn.frba.iasc.extensions

trait Kotlin {
  implicit class KtExtensions[A](a: A) {
    def let[B](f: A => B): B = f(a)
  }
}
