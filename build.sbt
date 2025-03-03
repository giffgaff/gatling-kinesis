import xerial.sbt.Sonatype.GitHubHosting

lazy val `gatling-kinesis` =
  project
    .in(file("."))
    .enablePlugins(GatlingPlugin)
    .settings(settings)
    .settings(
      name := "gatling-kinesis-alex",
      version:= "0.0.5",
      scalaVersion := "2.13.16",
      libraryDependencies ++= Seq(
      library.scalaCheck % Test,
      library.scalaTest % Test,
      library.scalaTestPlusScalaCheck % Test,
      library.gatlingHighCharts % Test,
      library.gatlingTestFramework % Test,
      library.gatlingCore,
      library.awsSdkCore,
      library.awsKinesisSdk,
      library.awsStsSdk,
      library.scalaJavaCompat,
    ) ++ library.dockerTestKit
    )
// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    object Version {
      val scalaCheck = "1.14.3"
      val scalaTest = "3.1.1"
      val scalaTestPlusScalaCheck = "3.1.1.1"
      val gatlingVersion = "3.13.4"
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

    val scalaJavaCompat = "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2"

    val dockerTestKit = Seq(
      "com.whisk" %% "docker-testkit-scalatest" % Version.dockerTestKit % "test",
      "com.whisk" %% "docker-testkit-impl-spotify" % Version.dockerTestKit % "test"
    )
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings = commonSettings ++ scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.13.1",
    organization := "com.giffgaff",
    organizationName := "giffgaff",
    startYear := Some(2025),
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
//
//lazy val publishSettings =
//  Seq(
//    publishTo := sonatypePublishToBundle.value,
//    sonatypeProfileName := "com.giffgaff",
//    publishMavenStyle := true,
//    sonatypeProjectHosting := Some(GitHubHosting("giffgaff", "gatling-kinesis", "alexanderb@giffgaff.co.uk"))
//  )

ThisBuild / organization := "com.giffgaff"
ThisBuild / organizationName := "giffgaff"
ThisBuild / organizationHomepage := Some(url("https://github.com/giffgaff/gatling-kinesis"))

publishTo := {
  val localMavenRepo = Path.userHome.absolutePath + "/.m2/repository"
  if (isSnapshot.value)
    Some("snapshots" at "file://" + localMavenRepo)
  else
    Some("releases" at "file://" + localMavenRepo + "/releases")
}

publishMavenStyle := true