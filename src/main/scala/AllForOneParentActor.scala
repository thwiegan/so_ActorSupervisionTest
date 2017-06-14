import akka.actor.SupervisorStrategy.Restart
import akka.actor.{ActorRef, AllForOneStrategy, SupervisorStrategy}

/**
  * Created by d058837 on 14.06.17.
  */
class AllForOneParentActor(recipient: ActorRef) extends ParentActor(recipient) {
  override def supervisorStrategy: SupervisorStrategy = AllForOneStrategy() {
    case _ => Restart
  }
}
