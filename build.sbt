name := """reactive-logs"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "apache-snapshots" at "http://repository.apache.org/snapshots"
)

libraryDependencies ++= Seq(
  ws,
  "commons-io" % "commons-io" % "2.5-SNAPSHOT",
  specs2 % Test
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

scalastyleConfig := file("conf/scalastyle-config.xml")
