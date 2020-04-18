package tubi.gatling.kinesis.protocol

import com.typesafe.scalalogging.StrictLogging
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{ Protocol, ProtocolKey }
import software.amazon.awssdk.services.kinesis.{ KinesisAsyncClient, KinesisAsyncClientBuilder }

object KinesisProtocol {
  val kinesisProtocolKey = new ProtocolKey[KinesisProtocol, KinesisComponents] with StrictLogging {
    override def protocolClass: Class[Protocol] =
      classOf[KinesisProtocol].asInstanceOf[Class[Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): KinesisProtocol =
      KinesisProtocol(KinesisAsyncClient.builder())

    override def newComponents(
        coreComponents: CoreComponents
    ): KinesisProtocol => KinesisComponents = { protocol =>
      KinesisComponents(protocol.kinesisBuilder)
    }
  }
}

case class KinesisProtocol(kinesisBuilder: KinesisAsyncClientBuilder) extends Protocol {
  type Components = KinesisComponents
}
