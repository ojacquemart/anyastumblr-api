import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "hfrtumlbr"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.jsoup" % "jsoup" % "1.7.2",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.8"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
  )

}
