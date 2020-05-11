package tubi.gatling.kinesis.action

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session._
import io.gatling.core.structure.ScenarioContext
import software.amazon.awssdk.core.SdkBytes

case class PutRecordActionBuilder(
    requestName: Expression[String],
    streamName: String,
    data: Option[Expression[SdkBytes]],
    partitionKey: Option[Expression[String]]
) extends ActionBuilder {
  def streamName(name: String): PutRecordActionBuilder = copy(streamName = name)

  def partitionKey(key: Expression[String]): PutRecordActionBuilder = copy(partitionKey = Some(key))

  def payload(@unchecked body: Expression[SdkBytes]): PutRecordActionBuilder =
    copy(data = Some(body))

  override def build(ctx: ScenarioContext, next: Action): Action =
    PutRecordAction(requestName, this, ctx, next)
}
