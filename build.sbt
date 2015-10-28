
name := "mwundo"
organization := "com.monsanto.labs"
version := "0.0.34"

bintrayOrganization := Some("monsanto")

licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause"))

scalaVersion := "2.11.6"

libraryDependencies ++= Dependencies.compile ++ Dependencies.test

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xcheckinit",
  "-Xlint",
  "-Xverify",
  "-Yclosure-elim",
  "-Yinline",
  "-Yno-adapted-args",
  "-encoding", "utf8",
  "-target:jvm-1.7")


testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
  Tests.Argument(TestFrameworks.ScalaTest, "-u", (target.value / "test-reports").getPath),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", (target.value / "test-reports-html").getPath)
)

PluginConfig.settings

