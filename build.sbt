
lazy val commonSettings = Seq(
  organization := "com.monsanto.labs",

  licenses += ("BSD", url("http://opensource.org/licenses/BSD-3-Clause")),

  crossScalaVersions := Seq("2.12.4", "2.11.8"),
  scalaVersion := crossScalaVersions.value.head,
  releaseCrossBuild := true,

  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xcheckinit",
    "-Xlint",
    "-Xverify",
    "-Yno-adapted-args",
    "-encoding", "utf8") ++
    scalacVersionOptions(scalaBinaryVersion.value),

  testOptions in Test ++= Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
    Tests.Argument(TestFrameworks.ScalaTest, "-u", (target.value / "test-reports").getPath),
    Tests.Argument(TestFrameworks.ScalaTest, "-h", (target.value / "test-reports-html").getPath)
  )
) ++ PluginConfig.gitStampSettings ++ PluginConfig.buildPropertiesSettings ++ PluginConfig.bintraySettings

lazy val scalacVersionOptions: Map[String, Seq[String]] = Map(
  "2.11" -> Seq(
    "-Yclosure-elim",
    "-Yinline",
    "-target:jvm-1.7"),
  "2.12" -> Seq.empty
)

lazy val mwundo = project.in(file("."))
  .aggregate(`mwundo-core`, `mwundo-spray`)
  .settings(commonSettings)
  .settings(Seq(
    packagedArtifacts := Map.empty
  ))

lazy val `mwundo-core` = project.in(file("core"))
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Dependencies.core ++ Dependencies.test
  ))

lazy val `mwundo-spray` = project.in(file("spray"))
  .dependsOn(`mwundo-core`)
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Dependencies.spray ++ Dependencies.test
  ))
