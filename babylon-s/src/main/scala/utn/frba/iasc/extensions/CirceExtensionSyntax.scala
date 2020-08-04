package utn.frba.iasc.extensions

import io.circe.{Decoder, Encoder}

trait CirceExtensionSyntax extends EitherSyntax {

  import io.circe.parser._
  import io.circe.syntax._

  def decodeOrThrow[A: Decoder](input: String): A = decode(input).getOrThrow

  def encode[A](output: A)(implicit encoder: Encoder[A]): String = output.asJson.noSpaces
}
