name := "hfrtumlbr"

version := "1.2.0-SNAPSHOT"

resolvers += "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/"

resolvers += "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"

libraryDependencies ++= Seq(
    cache,
    "com.newrelic.agent.java"   % "newrelic-agent"          % "2.21.3",
    "org.jsoup"                 % "jsoup"                   % "1.7.2",
    "org.mindrot"               % "jbcrypt"                 % "0.3m",
    "org.reactivemongo"         %% "play2-reactivemongo"    % "0.10.0-SNAPSHOT",
    "play-autosource"           %% "reactivemongo"          % "1.0-SNAPSHOT"
)

play.Project.playScalaSettings
