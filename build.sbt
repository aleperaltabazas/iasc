name := "babylon"

version := "0.1"

scalaVersion := "2.13.2"

val jacksonVersion = "2.9.7"
val guiceVersion = "4.2.2"
val sparkVersion = "2.9.1"

libraryDependencies ++= Seq(
  "com.sparkjava" % "spark-core" % sparkVersion,
  "com.google.inject" % "guice" % guiceVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % jacksonVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.6.6"
)