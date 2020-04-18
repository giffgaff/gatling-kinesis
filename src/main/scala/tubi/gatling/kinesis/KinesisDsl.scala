package tubi.gatling.kinesis

import io.gatling.core.session.Expression
import software.amazon.awssdk.services.kinesis.KinesisAsyncClientBuilder
import tubi.gatling.kinesis.action.PutRecordActionBuilder
import tubi.gatling.kinesis.protocol.KinesisProtocol

trait KinesisDsl {
  def kinesis(requestName: Expression[String]) = ReqWithoutStream(requestName)
  def kinesis(requestName: Expression[String], streamName: String) =
    ReqWithStream(requestName, streamName)
  def kinesis(builder: KinesisAsyncClientBuilder) = KinesisProtocol(builder)
}

case class ReqWithoutStream(requestName: Expression[String]) {
  def stream(name: String) = ReqWithStream(requestName, streamName = name)
}

case class ReqWithStream(requestName: Expression[String], streamName: String) {
  def putRecord() = PutRecordActionBuilder(requestName, streamName, None, None)
}
