package tubi.gatling.kinesis

import com.whisk.docker.impl.spotify.DockerKitSpotify
import com.whisk.docker.{ DockerContainer, DockerKit, DockerReadyChecker, LogLineReceiver }

trait DockerLocalStack extends DockerKit with DockerKitSpotify {
  private final val dockerImage        = "localstack/localstack:0.10.6"
  protected final val kinesisLocalPort = 4568

  /**
    * Decimal value between 0.0 (default) and 1.0 to randomly inject ProvisionedThroughputExceededException errors into
    * Kinesis API responses
    */
  protected val kinesisErrorRate: Double = 0.0

  val localstackContainer = DockerContainer(dockerImage)
    .withPorts((4568, Some(kinesisLocalPort)))
    .withEnv(s"""SERVICES=kinesis
                |KINESIS_PORT_EXTERNAL=$kinesisLocalPort
                |KINESIS_ERROR_PROBABILITY=$kinesisErrorRate
                |KINESIS_LATENCY=0
                |DEBUG=1""".stripMargin.split("\n"): _*)
    .withReadyChecker(DockerReadyChecker.LogLineContains("Ready."))

  override def dockerContainers = localstackContainer :: super.dockerContainers
}
