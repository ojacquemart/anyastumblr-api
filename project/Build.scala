import sbt._

import Keys._
import play.Project._

import de.johoop.jacoco4sbt._
import JacocoPlugin._

object ApplicationBuild extends Build {

  val appName = "hfrtumlbr"
  val appVersion = "1.1.0-SNAPSHOT"

  lazy val s = Defaults.defaultSettings ++ Seq(jacoco.settings:_*)

  val appDependencies = Seq(
    "org.jsoup" % "jsoup" % "1.7.2",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.8"
  )

  val main = play.Project(appName, appVersion, appDependencies, settings = s).settings(
    parallelExecution     in jacoco.Config := false,
    jacoco.reportFormats  in jacoco.Config := Seq(XMLReport("utf-8"), HTMLReport("utf-8")),
    jacoco.excludes       in jacoco.Config := Seq("views.*", "controllers.Reverse*", "controllers.javascript.*", "controllers.ref.*", "Routes*")
  )

}
