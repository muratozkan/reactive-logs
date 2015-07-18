
name := """core"""

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
  "commons-io" % "commons-io" % "2.5-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "2.2.5" % Test
)

