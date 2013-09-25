import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "hfrastumblr"
  val appVersion      = "1.2.0-SNAPSHOT"

  val mainDeps = Seq(
    cache,
    "com.newrelic.agent.java"   % "newrelic-agent"          % "2.21.3",
    "org.jsoup"                 % "jsoup"                   % "1.7.2",
    "org.mindrot"               % "jbcrypt"                 % "0.3m",
    "org.reactivemongo"         %% "play2-reactivemongo"    % "0.10.0-SNAPSHOT",
    "play-autosource"           %% "reactivemongo"          % "1.0-SNAPSHOT"
  )

  val adminDeps = Seq(cache)

  lazy val common = play.Project(appName + "-common", appVersion, adminDeps, path = file("common"))
  lazy val admin = play.Project(appName + "-admin", appVersion, Seq(), path = file("modules/admin")).dependsOn(common)


  lazy  val main = play.Project(appName, appVersion, mainDeps).settings(
    resolvers += "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    resolvers += "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"
  ).dependsOn(admin).aggregate(admin)

}