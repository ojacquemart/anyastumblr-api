import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "hfrtumlbr"
  val appVersion = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.jsoup" % "jsoup" % "1.7.1",
      "play.modules.reactivemongo" %% "play2-reactivemongo" % "0.1-SNAPSHOT"  cross CrossVersion.full
    )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    //resolvers += ("mandubian-mvn snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots")
    resolvers += "sgodbillon" at "https://bitbucket.org/sgodbillon/repository/raw/master/snapshots/"
  )

}
