name := "soactortest"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-stream" % "2.5.2",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.2" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test
)
