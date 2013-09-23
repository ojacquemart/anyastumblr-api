import sbt._

import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "hfrtumlbr"
  val appVersion = "1.1.0-SNAPSHOT"

  val mandubianRepo = Seq(
    "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"
  )
  val secureSocialRepo = Seq(
    Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

  val appDependencies = Seq(
    "com.newrelic.agent.java" % "newrelic-agent"        % "2.21.3",
    "org.jsoup"               % "jsoup"                 % "1.7.2",
    ("org.reactivemongo"       %% "play2-reactivemongo"  % "0.10.0-SNAPSHOT")
      .exclude("play", "play"),
    ("play-autosource"         %% "reactivemongo"        % "0.11-SNAPSHOT")
      .exclude("org.scala-stm", "scala-stm_2.10.0")
      .exclude("play", "play"),
    ("securesocial"            %% "securesocial"         % "master-SNAPSHOT")
      .exclude("org.scala-stm", "scala-stm_2.10.0")
      .exclude("play", "play")
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= mandubianRepo,
    resolvers ++= secureSocialRepo
  )

}
