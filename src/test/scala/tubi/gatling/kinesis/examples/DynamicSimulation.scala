package tubi.gatling.kinesis.examples

import java.util.UUID

import io.gatling.commons.validation.Success
import io.gatling.core.Predef._
import software.amazon.awssdk.core.SdkBytes
import tubi.gatling.kinesis.BaseKinesisSimulation
import tubi.gatling.kinesis.Predef._
import tubi.gatling.kinesis.protocol.KinesisProtocol

import scala.util.Random

// a slightly more complex example that shows how you can pull values from the per-user session to create more
// dynamic payloads. The session for each user here is populated by the feeder, but it could have very well be
// populated by a previous step
// you can run this with gatling:testOnly tubi.gatling.kinesis.examples.DynamicSimulation
class DynamicSimulation extends BaseKinesisSimulation {
  val kinesisConfig: KinesisProtocol        = kinesis(kinesisLocalClientBuilder(kinesisLocalPort))
  override protected val streamName: String = "some-stream-2"

  val feeder = Iterator.continually(
    Map(
      "some-key"          -> UUID.randomUUID().toString,
      "some-payload-text" -> Random.nextString(5)
    )
  )

  val testScenario = scenario("showcase dynamic input")
    .feed(feeder)
    .exec(
      kinesis("single record")
        .stream(streamName)
        .putRecord()
        .partitionKey(s => Success(s("some-key").as[String]))
        .payload { s =>
          val data = SdkBytes.fromUtf8String(s("some-payload-text").as[String])
          Success(data)
        }
    )

  setUp(testScenario.inject(atOnceUsers(10)))
    .protocols(kinesisConfig)
    .assertions(global.failedRequests.count.is(0))
}
