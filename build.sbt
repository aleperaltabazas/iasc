name := "babylon"

version := "0.1"

scalaVersion := "2.13.2"

val jacksonVersion = "2.9.7"
val guiceVersion = "4.2.2"
val sparkVersion = "2.9.1"
val vSlf4J = "1.7.30"
val vLogback = "1.2.1"
val vDespLogging = "0.0.4"

libraryDependencies ++= Seq(
  "com.sparkjava" % "spark-core" % sparkVersion,
  "com.google.inject" % "guice" % guiceVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % jacksonVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.6.6",
  "org.slf4j" % "slf4j-api" % vSlf4J,
  "ch.qos.logback" % "logback-classic" % vLogback,
  "org.slf4j" % "log4j-over-slf4j" % vSlf4J,
)