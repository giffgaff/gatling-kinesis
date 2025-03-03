package tubi.gatling.kinesis.action

import io.gatling.commons.util.Clock
import io.gatling.commons.validation.Validation
import io.gatling.core.action.Action
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext
import software.amazon.awssdk.services.kinesis.model.{ PutRecordRequest, PutRecordResponse }
import tubi.gatling.kinesis.protocol.KinesisProtocol

import scala.compat.java8.FutureConverters._
import scala.concurrent.{ ExecutionContext, Future }

case class PutRecordAction(
    requestName: Expression[String],
    reqBuilder: PutRecordActionBuilder,
    ctx: ScenarioContext,
    next: Action
) extends KinesisAction {
  def clock: Clock             = ctx.coreComponents.clock
  def statsEngine: StatsEngine = ctx.coreComponents.statsEngine

  private[this] val kinesisComponents =
    ctx.protocolComponentsRegistry.components(KinesisProtocol.kinesisProtocolKey)

  override def name: String = genName("kinesisPutRecord")

  override def execute(session: Session): Unit = {
    val start = clock.nowMillis
    logger.trace("executing put record action")

    val resp: Validation[Future[PutRecordResponse]] = for {
      pKey    <- reqBuilder.partitionKey.get.apply(session)
      payload <- reqBuilder.data.get.apply(session)
    } yield {
      val req = PutRecordRequest
        .builder()
        .streamName(reqBuilder.streamName)
        .data(payload)
        .partitionKey(pKey)
        .build()

      kinesisComponents.kinesisClient.putRecord(req).toScala
    }

    implicit val ec: ExecutionContext = ctx.coreComponents.actorSystem.executionContext

    resp.onSuccess(_.onComplete { result =>
      next ! logResult(start, clock.nowMillis, result, requestName, session, statsEngine)
    })

    resp.onFailure { s =>
      // this scenario shouldn't really happen unless somehow we couldn't even construct the future
      logger.error("Failed to even make a request, error={}", s)
      next ! session.markAsFailed
    }
  }
}
