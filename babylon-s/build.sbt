name := "babylon"

version := "0.1"

scalaVersion := "2.13.2"

val guiceVersion = "4.2.2"
val vSlf4J = "1.7.30"
val vLogback = "1.2.1"
val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % guiceVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",

  "com.typesafe.akka" %% "akka-actor" % "2.6.3",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.6.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.6.3",
  "com.typesafe.akka" %% "akka-distributed-data" % "2.6.3",

  "org.slf4j" % "slf4j-api" % vSlf4J,
  "ch.qos.logback" % "logback-classic" % vLogback,
  "org.slf4j" % "log4j-over-slf4j" % vSlf4J,
)