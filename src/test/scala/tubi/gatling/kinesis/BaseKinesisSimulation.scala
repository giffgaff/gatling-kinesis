package tubi.gatling.kinesis

import java.net.URI
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import io.gatling.core.Predef.Simulation
import software.amazon.awssdk.auth.credentials.{ AwsBasicCredentials, StaticCredentialsProvider }
import software.amazon.awssdk.http.Protocol
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest
import software.amazon.awssdk.services.kinesis.{ KinesisAsyncClient, KinesisClient }

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Just a base class to take care of starting/stopping docker containers and other aspects that have nothing to
  * do with Gatling itself
  */
abstract class BaseKinesisSimulation extends Simulation with DockerLocalStack with LazyLogging {
  // need this because localstack kinesis does not support binary CBOR protocol
  System.setProperty("aws.cborEnabled", "false")

  // having a sync client is handy for doing maintenance operations like creating/destroying streams
  final val kinesisSyncClient: KinesisClient = KinesisClient
    .builder()
    .endpointOverride(new URI(s"http://localhost:$kinesisLocalPort"))
    .credentialsProvider(
      StaticCredentialsProvider.create(AwsBasicCredentials.create("abc", "abcd"))
    )
    .build()
  protected val streamName: String

  before {
    logger.info("Starting kinesis container")
    startAllOrFail()
    assert(dockerContainers.forall(container => Await.result(container.isReady(), 2.minutes)))

    logger.info(s"Creating stream $streamName")
    val req = CreateStreamRequest.builder().streamName(streamName).shardCount(1).build()
    kinesisSyncClient.createStream(req)
  }

  after {
    logger.info("Stopping kinesis container")
    stopAllQuietly()
  }

  /**
    * Helper method to create a Kinesis async client specifically for testing against mock kinesis running on localhost
    * @return
    */
  def kinesisLocalClientBuilder =
    KinesisAsyncClient
      .builder()
      .endpointOverride(new URI(s"http://localhost:$kinesisLocalPort"))
      .httpClientBuilder(
        NettyNioAsyncHttpClient
          .builder()
          .protocol(Protocol.HTTP1_1)
      )
      .credentialsProvider(
        StaticCredentialsProvider.create(AwsBasicCredentials.create("abc", "abcd"))
      )
}
