import akka.actor.SupervisorStrategy.Restart
import akka.actor.{ActorRef, OneForOneStrategy, SupervisorStrategy}

/**
  * Created by d058837 on 14.06.17.
  */
class OneForOneParentActor(recipient: ActorRef) extends ParentActor(recipient) {
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }
}
