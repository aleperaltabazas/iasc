package utn.frba.iasc.utils

import java.time.LocalDateTime

class Clock {
  def now: LocalDateTime = LocalDateTime.now()

  def afterSeconds(n: Int): LocalDateTime = now.plusSeconds(n)
}
