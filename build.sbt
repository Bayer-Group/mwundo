
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}


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

lazy val breeze = "0.13"
lazy val circe = "0.9.3"

lazy val mwundo = project.in(file("."))
  .aggregate(`mwundo-coreJVM`,`mwundo-coreJS`,`mwundo-spray`, `mwundo-circeJVM`,`mwundo-circeJS`)
  .settings(commonSettings)
  .settings(Seq(
    packagedArtifacts := Map.empty,
    publishArtifact := false,
    publish := {}
  ))

lazy val `mwundo-core` = crossProject(JSPlatform, JVMPlatform).in(file("core"))
 // .crossType(CrossType.Pure)
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "org.scalatest"           %% "scalatest"                    % "3.0.1" % "test",
      "org.scalacheck"          %% "scalacheck"                   % "1.13.4" % "test",
      "org.scalamock"           %% "scalamock-scalatest-support"  % "3.5.0" % "test",
      "org.pegdown"             %  "pegdown"                      % "1.4.2" % "test"
    )
  )).jvmSettings(Seq(
  libraryDependencies ++= Seq(
    "org.scalanlp"       %% "breeze"         % breeze,
    "org.scalanlp"       %% "breeze-natives" % breeze,
    "com.vividsolutions" %  "jts-core"       % "1.14.0")))

lazy val `mwundo-coreJVM`= `mwundo-core`.jvm
lazy val `mwundo-coreJS`= `mwundo-core`.js

lazy val `mwundo-spray` = project.in(file("spray"))
  .dependsOn(`mwundo-coreJVM`)
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-json" % "1.3.2",
      "org.scalatest"           %% "scalatest"                    % "3.0.1" % "test",
      "org.scalacheck"          %% "scalacheck"                   % "1.13.4" % "test",
      "org.scalamock"           %% "scalamock-scalatest-support"  % "3.5.0" % "test",
      "org.pegdown"             %  "pegdown"                      % "1.4.2" % "test"
    )
  ))


lazy val `mwundo-circe` = crossProject(JSPlatform, JVMPlatform)
  .in(file("circe"))
 // .crossType(CrossType.Pure)
  .dependsOn(`mwundo-core`)
  .settings(commonSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circe,
      "io.circe" %%% "circe-generic" % circe,
      "io.circe" %%% "circe-parser" % circe,
      "org.scalatest"           %% "scalatest"                    % "3.0.1" % "test",
      "org.scalacheck"          %% "scalacheck"                   % "1.13.4" % "test",
      "org.scalamock"           %% "scalamock-scalatest-support"  % "3.5.0" % "test",
      "org.pegdown"             %  "pegdown"                      % "1.4.2" % "test"
    )
  )
  )

lazy val `mwundo-circeJVM`= `mwundo-circe`.jvm
lazy val `mwundo-circeJS`= `mwundo-circe`.js