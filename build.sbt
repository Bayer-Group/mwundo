
name := "mwundo"
organization := "com.monsanto.labs"
version := "0.1.2"

bintrayOrganization := Some("monsanto")

licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause"))

crossScalaVersions := Seq("2.11.8", "2.12.1")
scalaVersion := "2.12.1"

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
