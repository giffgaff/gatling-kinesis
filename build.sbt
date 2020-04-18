
lazy val `gatling-kinesis` =
  project
    .in(file("."))
    .enablePlugins(GatlingPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.scalaCheck % Test,
        library.scalaTest % Test,
        library.scalaTestPlusScalaCheck % Test,
        library.gatlingHighCharts % Test,
        library.gatlingTestFramework % Test,
        library.gatlingCore,
        library.awsSdkCore,
        library.awsKinesisSdk,
        library.awsStsSdk
      ) ++ library.dockerTestKit
    )
//    .settings(inConfig(Gatling)(Defaults.testSettings))
// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    object Version {
      val scalaCheck = "1.14.3"
      val scalaTest = "3.1.1"
      val scalaTestPlusScalaCheck = "3.1.1.1"
      val gatlingVersion = "3.3.1"
      val awsSdk = "2.11.12"
      val dockerTestKit = "0.9.9"
    }

    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
    val scalaTestPlusScalaCheck = "org.scalatestplus" %% "scalacheck-1-14" % Version.scalaTestPlusScalaCheck

    val awsSdkCore = "software.amazon.awssdk" % "aws-core" % Version.awsSdk
    val awsKinesisSdk = "software.amazon.awssdk" % "kinesis" % Version.awsSdk
    val awsStsSdk = "software.amazon.awssdk" % "sts" % Version.awsSdk
    val gatlingCore = "io.gatling" % "gatling-core" % Version.gatlingVersion
    val gatlingHighCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % Version.gatlingVersion
    val gatlingTestFramework = "io.gatling" % "gatling-test-framework" % Version.gatlingVersion

    val dockerTestKit = Seq(
      "com.whisk" %% "docker-testkit-scalatest" % Version.dockerTestKit % "test",
      "com.whisk" %% "docker-testkit-impl-spotify" % Version.dockerTestKit % "test"
    )
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
    organization := "tubi",
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
    parallelExecution in Test := false
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )
