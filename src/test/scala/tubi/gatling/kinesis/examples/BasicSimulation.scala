package tubi.gatling.kinesis.examples

import io.gatling.core.Predef._
import tubi.gatling.kinesis.BaseKinesisSimulation
import tubi.gatling.kinesis.Predef._
import tubi.gatling.kinesis.protocol.KinesisProtocol

// you can run this in sbt with gatling:test
class BasicSimulation extends BaseKinesisSimulation {
  val kinesisConfig: KinesisProtocol        = kinesis(kinesisLocalClientBuilder(kinesisLocalPort))
  override protected val streamName: String = "some-stream"

  val testScenario = scenario("showcase kinesis plugin")
    .exec(
      kinesis("single record")
        .stream(streamName)
        .putRecord()
        .payload("test body")
        .partitionKey("test pkey")
    )

  setUp(testScenario.inject(atOnceUsers(1)))
    .protocols(kinesisConfig)
    .assertions(global.failedRequests.count.is(0))
}
