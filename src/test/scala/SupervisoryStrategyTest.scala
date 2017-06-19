import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class SupervisorStrategiesTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with Matchers with ImplicitSender {

  import StateHoldingActor._

  "actor with custom supervision strategy" should {
    "apply the strategy to a single child" in {
      implicit val timeout: Timeout = 3 seconds

      val parentActor = system.actorOf(Props(new OneForOneParentActor(testActor)))

      val initialStateFuture = parentActor ? "state"
      val initialState = Await.result(initialStateFuture, timeout.duration)
      initialState shouldBe List(Vector(), Vector())

      parentActor ! ("first", AddStateCommand(1))
      parentActor ! ("second", AddStateCommand(2))

      val currentStateFuture = parentActor ? "state"
      val currentState = Await.result(currentStateFuture, timeout.duration)
      currentState shouldBe List(Vector(1), Vector(2))

      parentActor ! "throwFirst"

      val stateAfterRestartFuture = parentActor ? "state"
      val stateAfterRestart = Await.result(stateAfterRestartFuture, timeout.duration)
      stateAfterRestart shouldBe List(Vector(), Vector(2))
    }

    "apply the strategy to all children" in {
      implicit val timeout: Timeout = 3 seconds

      val parentActor = system.actorOf(Props(new AllForOneParentActor(testActor)))

      val initialStateFuture = parentActor ? "state"
      val initialState = Await.result(initialStateFuture, timeout.duration)
      initialState shouldBe List(Vector(), Vector())

      parentActor ! ("first", AddStateCommand(3))
      parentActor ! ("second", AddStateCommand(4))

      val currentStateFuture = parentActor ? "state"
      val currentState = Await.result(currentStateFuture, timeout.duration)
      currentState shouldBe List(Vector(3), Vector(4))

      parentActor ! "throwFirst"

      val stateAfterRestartFuture = parentActor ? "state"
      val stateAfterRestart = Await.result(stateAfterRestartFuture, timeout.duration)
      stateAfterRestart shouldBe List(Vector(), Vector(4))

      //Actor 2 restarts here

      val blankStateAfterRestartFuture = parentActor ? "state"
      val blankStateAfterRestart = Await.result(blankStateAfterRestartFuture, timeout.duration)
      blankStateAfterRestart shouldBe List(Vector(), Vector())
    }
  }
}