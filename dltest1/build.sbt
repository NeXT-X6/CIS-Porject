name := "test"

version := "1.0"

scalaVersion := "2.12.7"

lazy val akkaVersion = "2.6.0-M5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "junit" % "junit" % "4.12")
