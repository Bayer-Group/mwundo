addSbtPlugin("com.atlassian.labs" %  "sbt-git-stamp"          % "0.1.2")
addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin"  % "0.8.0")
addSbtPlugin("org.scoverage"      %  "sbt-scoverage"          % "1.5.0")
//TODO this crashes builds with binary incompatibilty
//addSbtPlugin("org.scoverage"      %  "sbt-coveralls"          % "1.1.0")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.7")
