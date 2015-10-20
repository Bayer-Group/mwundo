import sbt.Keys.{name, sbtVersion, scalaVersion, sourceGenerators, version, _}
import sbt.{Compile, SettingKey, _}

object PluginConfig {

  lazy val gitBranch = SettingKey[String]("gitBranch")
  lazy val gitRevision = SettingKey[String]("gitRevision")
  lazy val buildTimestamp = SettingKey[String]("buildTimestamp")

  lazy val settings =
    gitStampSettings ++
    buildPropertiesSettings

  private lazy val gitStampSettings = {
    import com.atlassian.labs.gitstamp.GitStampPlugin

    GitStampPlugin.gitStampSettings ++ Seq(
      gitBranch := GitStampPlugin.repoInfo(0)._2,
      gitRevision := GitStampPlugin.repoInfo(2)._2,
      buildTimestamp := GitStampPlugin.repoInfo(3)._2
    )
  }

  private lazy val buildPropertiesSettings =
    resourceGenerators in Compile <+=
      (resourceManaged in Compile, name, version, scalaVersion, sbtVersion, gitBranch, gitRevision, buildTimestamp) map {
        (dir, name, version, scalaVersion, sbtVersion, gitBranch, gitRevision, buildTimestamp) =>
          val file = dir / "geojson" / "build.properties"

          import scala.util.Properties
          val contents =
            s"""name=$name
                |version=$version
                |scala.version=$scalaVersion
                |sbt.version=$sbtVersion
                |git.branch=$gitBranch
                |git.revision=$gitRevision
                |build.timestamp=$buildTimestamp
                |build.user=${Properties.userName}""".stripMargin

          IO.write(file, contents)
          Seq(file)
      }
}
