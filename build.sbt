
name := """reactive-logs"""

version := "0.0.1"

scalaVersion := "2.11.7"

lazy val commonSettings = Seq(
  organization := "com.reactivelogs",
  version := "0.0.1",
  scalaVersion := "2.11.7",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-target:jvm-1.8",
    "-encoding", "utf8")
)

val commonResolvers = Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "apache-snapshots" at "http://repository.apache.org/snapshots"
)

lazy val core = (project in file ("core"))
  .settings(commonSettings:_*)
  .settings(resolvers ++= commonResolvers)

lazy val playApp = (project in file ("playApp"))
  .settings(commonSettings:_*)
  .settings(resolvers ++= commonResolvers)
  .enablePlugins(PlayScala)
  .dependsOn(core)

scalastyleConfig := file("config/scalastyle-config.xml")

lazy val versionReport = TaskKey[String]("version-report")


