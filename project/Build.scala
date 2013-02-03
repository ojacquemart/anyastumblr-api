import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "hfrtumlbr"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.jsoup" % "jsoup" % "1.7.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    )

}
