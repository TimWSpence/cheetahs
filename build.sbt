ThisBuild / baseVersion := "0.1.0"

ThisBuild / organization := "io.github.timwspence"
ThisBuild / organizationName := "TimWSpence"
publishGithubUser in ThisBuild := "TimWSpence"
publishFullName in ThisBuild := "Tim Spence"

ThisBuild / developers := List(
  Developer("TimWSpence", "Tim Spence", "@TimWSpence", url("https://github.com/TimWSpence"))
)

val PrimaryOS = "ubuntu-latest"

val Scala3 = "3.0.0-RC2"

ThisBuild / crossScalaVersions := Seq("3.0.0-M3", Scala3)

val LTSJava = "adopt@1.11"
val LatestJava = "adopt@1.15"
val GraalVM8 = "graalvm-ce-java8@20.2.0"

ThisBuild / githubWorkflowJavaVersions := Seq(LTSJava, LatestJava, GraalVM8)
ThisBuild / githubWorkflowOSes := Seq(PrimaryOS)

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("${{ matrix.ci }}")),

  WorkflowStep.Sbt(
    List("docs/mdoc"),
    cond = Some(s"matrix.scala == '$Scala3' && matrix.ci == 'ciJVM'")),
)

ThisBuild / githubWorkflowBuildMatrixAdditions += "ci" -> List("ciJVM")

ThisBuild / homepage := Some(url("https://github.com/TimWSpence/cheetahs"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/TimWSpence/cheetahs"),
    "git@github.com:TimWSpence/cheetahs.git"))

addCommandAlias("ciJVM", "; project cheetahs; headerCheck; scalafmtCheck; clean; test; mimaReportBinaryIssues")

addCommandAlias("prePR", "; project `cheetahs`; clean; scalafmtAll; headerCreate")

val DisciplineVersion = "1.0.6"
val ScalaCheckVersion = "1.15.3"
val MunitVersion = "0.7.23"
val ShapelessVersion = "3.0.0-M2"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.4"
inThisBuild(
  List(
    scalaVersion := Scala3,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

lazy val `cheetahs` = project.in(file("."))
  .settings(commonSettings)
  .aggregate(
    core,
    benchmarks,
    docs
  )
  .settings(noPublishSettings)

lazy val core = project.in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "cheetahs",
    scalacOptions := scalacOptions.value.filterNot(_ == "-source:3.0-migration") :+ "-source:future",
  )
  .settings(testFrameworks += new TestFramework("munit.Framework"))

lazy val benchmarks = project.in(file("benchmarks"))
  .settings(commonSettings)
  .dependsOn(core)
  .enablePlugins(NoPublishPlugin, JmhPlugin)


lazy val docs = project.in(file("cheetahs-docs"))
  .settings(
    moduleName := "cheetahs-docs",
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(core),
    target in (ScalaUnidoc, unidoc) := (baseDirectory in LocalRootProject).value / "website" / "static" / "api",
    cleanFiles += (target in (ScalaUnidoc, unidoc)).value,
    docusaurusCreateSite := docusaurusCreateSite.dependsOn(unidoc in Compile).value,
    docusaurusPublishGhpages := docusaurusPublishGhpages.dependsOn(unidoc in Compile).value,
  )
  .settings(commonSettings, skipOnPublishSettings)
  .enablePlugins(MdocPlugin, DocusaurusPlugin, ScalaUnidocPlugin)
  .dependsOn(core)


lazy val commonSettings = Seq(
  organizationHomepage := Some(url("https://github.com/TimWSpence")),
  libraryDependencies ++= Seq(
    "org.typelevel"              %% "shapeless3-deriving"       % ShapelessVersion,
    "org.scalacheck"             %% "scalacheck"                % ScalaCheckVersion % Test,
    "org.scalameta"              %% "munit"                     % MunitVersion % Test,
    "org.scalameta"              %% "munit-scalacheck"          % MunitVersion % Test,
  )
)

lazy val skipOnPublishSettings = Seq(
  skip in publish := true,
  publish := (()),
  publishLocal := (()),
  publishArtifact := false,
  publishTo := None
)
