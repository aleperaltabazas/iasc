package utn.frba.iasc.extensions

trait Kotlin {
  implicit class KtExtensions[A](a: A) {
    def let[B](f: A => B): B = f(a)

    def also(f: A => Unit): A = {
      f(a)
      a
    }
  }
}
