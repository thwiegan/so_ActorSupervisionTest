import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by d058837 on 14.06.17.
  */

object StateHoldingActor {

  case object ActorThrowCommand

  case class AddStateCommand(stateElement: Int)

  case object GetStateCommand

  case object GetStateCommandWithResponse

  def props(): Props = Props(new StateHoldingActor())
}

class StateHoldingActor() extends Actor with ActorLogging {
  log.info("about to create state")
  private var state = Vector[Int]()
  log.info(s"state created: $state")

  import StateHoldingActor._


  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)

    log.info(s"Restarting actor with state: $state")
  }

  override def receive: Receive = {
    case AddStateCommand(i) =>
      log.info(s"extending state: $state")
      state = i +: state
      log.info(s"extended state: $state")
    case GetStateCommand =>
      log.info(s"returning state: $state")
      sender ! state
    case GetStateCommandWithResponse =>
      log.info(s"returning state in response: $state")
      sender ! state
    case ActorThrowCommand =>
      log.info(s"throwing exception with state: $state")
      throw new IllegalStateException("Should crash actor instance and restart state")

  }

}