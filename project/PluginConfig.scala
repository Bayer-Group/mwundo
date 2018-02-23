import bintray.BintrayPlugin.autoImport.{bintrayOrganization, bintrayReleaseOnPublish}
import sbt.Keys.{name, sbtVersion, scalaVersion, version, _}
import sbt.{Compile, SettingKey, _}

object PluginConfig {

  private lazy val gitBranch = SettingKey[String]("gitBranch")
  private lazy val gitRevision = SettingKey[String]("gitRevision")
  private lazy val buildTimestamp = SettingKey[String]("buildTimestamp")

  lazy val bintraySettings = Seq(
    // for bintray

    bintrayOrganization := Some("monsanto"),

    licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause")),

    bintrayReleaseOnPublish := !isSnapshot.value,

    publishTo := {
      if (isSnapshot.value)
        Some("Artifactory Realm" at "https://oss.jfrog.org/oss-snapshot-local/")
      else
        publishTo.value /* Value set by bintray-sbt plugin */
    },

    credentials := {
      if (isSnapshot.value)
        List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
      else
        credentials.value /* Value set by bintray-sbt plugin */
    }
  )

  lazy val gitStampSettings = {
    import com.atlassian.labs.gitstamp.GitStampPlugin

    GitStampPlugin.gitStampSettings ++ Seq(
      gitBranch := GitStampPlugin.repoInfo(0)._2,
      gitRevision := GitStampPlugin.repoInfo(2)._2,
      buildTimestamp := GitStampPlugin.repoInfo(3)._2
    )
  }

  lazy val buildPropertiesSettings =
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
