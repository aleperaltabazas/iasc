package utn.frba.iasc.extensions

import io.circe.Decoder

trait CirceExtensionSyntax extends EitherSyntax {

  import io.circe.parser._

  def decodeOrThrow[A: Decoder](input: String): A = decode(input).getOrThrow
}
