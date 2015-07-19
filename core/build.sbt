
name := """core"""

val akkaVersion = "2.3.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2",
  "commons-io" % "commons-io" % "2.5-SNAPSHOT",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "2.2.5" % Test
)
