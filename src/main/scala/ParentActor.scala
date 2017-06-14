import StateHoldingActor.{ActorThrowCommand, AddStateCommand, GetStateCommand}
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by d058837 on 14.06.17.
  */
abstract class ParentActor(recipient: ActorRef) extends Actor with ActorLogging {
  log.info("creating children")
  private val stateHoldingActor1 = context
    .actorOf(StateHoldingActor.props())
  private val stateHoldingActor2 = context
    .actorOf(StateHoldingActor.props())
  log.info("children created")

  implicit val timeout: Timeout = 3 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case "throwFirst" =>
      log.info("stateHoldingActor1 ! ActorThrowCommand")
      stateHoldingActor1 ! ActorThrowCommand
    case "throwSecond" =>
      log.info("stateHoldingActor1 ! ActorThrowCommand")
      stateHoldingActor2 ! ActorThrowCommand
    case "state" =>
      log.info("gathering states")
      val futureResults: Future[List[Any]] = Future
        .sequence(List(stateHoldingActor1 ? GetStateCommand, stateHoldingActor2 ? GetStateCommand))
      import akka.pattern.pipe
      futureResults pipeTo sender()

    case ("first", msg@AddStateCommand(_)) => stateHoldingActor1 forward msg
    case ("second", msg@AddStateCommand(_)) => stateHoldingActor2 forward msg
  }
}
