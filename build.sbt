// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `gatling-kinesis` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.scalaCheck              % Test,
        library.scalaTest               % Test,
        library.scalaTestPlusScalaCheck % Test,
      ),
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaCheck              = "1.14.3"
      val scalaTest               = "3.1.1"
      val scalaTestPlusScalaCheck = "3.1.1.1"
    }
    val scalaCheck              = "org.scalacheck"    %% "scalacheck"      % Version.scalaCheck
    val scalaTest               = "org.scalatest"     %% "scalatest"       % Version.scalaTest
    val scalaTestPlusScalaCheck = "org.scalatestplus" %% "scalacheck-1-14" % Version.scalaTestPlusScalaCheck
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.13.1",
    organization := "io.tubi",
    organizationName := "Marios",
    startYear := Some(2020),
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-encoding", "UTF-8",
      "-Ywarn-unused:imports",
    ),
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )
