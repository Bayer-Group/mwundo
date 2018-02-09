
name := "mwundo"
organization := "com.monsanto.labs"

bintrayOrganization := Some("monsanto")

licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause"))

crossScalaVersions := Seq("2.11.8", "2.12.4")
scalaVersion := "2.12.4"
releaseCrossBuild := true

libraryDependencies ++= Dependencies.compile ++ Dependencies.test

val scalacVersionOptions: Map[String, Seq[String]] = Map(
  "2.11" -> Seq(
    "-Yclosure-elim",
    "-Yinline",
    "-target:jvm-1.7"),
  "2.12" -> Seq.empty
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xcheckinit",
  "-Xlint",
  "-Xverify",
  "-Yno-adapted-args",
  "-encoding", "utf8") ++
  scalacVersionOptions(scalaBinaryVersion.value)


testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
  Tests.Argument(TestFrameworks.ScalaTest, "-u", (target.value / "test-reports").getPath),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", (target.value / "test-reports-html").getPath)
)

PluginConfig.settings

// for bintray

bintrayOrganization := Some("monsanto")

licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause"))

bintrayReleaseOnPublish := ! isSnapshot.value

publishTo := {
  if (isSnapshot.value)
    Some("Artifactory Realm" at "https://oss.jfrog.org/oss-snapshot-local/")
  else
    publishTo.value /* Value set by bintray-sbt plugin */
}

credentials := {
  if (isSnapshot.value)
    List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
  else
    credentials.value /* Value set by bintray-sbt plugin */
}
