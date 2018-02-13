import sbt._

object Dependencies {

  private val breeze = "0.13"

  lazy val core = Seq(
    "com.vividsolutions" %  "jts-core"       % "1.14.0",
    // -- breeze --
    "org.scalanlp"       %% "breeze"         % breeze,
    "org.scalanlp"       %% "breeze-natives" % breeze
  )

  lazy val spray = Seq(
    "io.spray" %% "spray-json" % "1.3.2"
  )

  lazy val test = Seq(
    "org.scalatest"           %% "scalatest"                    % "3.0.1",
    "org.scalacheck"          %% "scalacheck"                   % "1.13.4",
    "org.scalamock"           %% "scalamock-scalatest-support"  % "3.5.0",
    "org.pegdown"             %  "pegdown"                      % "1.4.2"
  ).map(_ % "test")
}
