package tubi.gatling.kinesis.action

import io.gatling.commons.stats.{ KO, OK }
import io.gatling.core.action.ChainableAction
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import software.amazon.awssdk.services.kinesis.model.KinesisResponse

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success, Try }

/**
  * Trait for all actions in this extension
  */
trait KinesisAction extends ChainableAction with NameGen {

  def logResult(
      start: Long,
      end: Long,
      result: Try[KinesisResponse],
      requestName: Expression[String],
      session: Session,
      statsEngine: StatsEngine
  )(implicit ec: ExecutionContext): Session = {
    val (status, message, newSession) = result match {
      case Success(_)  => (OK, None, session.markAsSucceeded)
      case Failure(ex) => (KO, Some(ex.getMessage), session.markAsFailed)
    }

    session.logGroupRequestTimings(start, end)
    requestName.apply(session).foreach { resolvedRequestName =>
      statsEngine.logResponse(newSession, resolvedRequestName, start, end, status, None, message)
    }
    newSession
  }
}
