package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class AccountLoadTest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:4567")
    .header("Content-Type", "application/json")

  val scn = scenario("Account Load Test")
    .exec(
      http("Transfer Money")
        .post("/accounts/transfer") // 🔹 Update the endpoint if needed
        .body(StringBody("""{"from": "accountA_ID", "to": "accountB_ID", "amount": "100.00"}"""))
        .asJson
        .check(status.is(200))
    )
    .pause(2.seconds)

  setUp(
    scn.inject(
      rampUsers(10) during (1.minute), // 1 min ramp-up
      constantUsersPerSec(10.0 / 60) during (9.minutes) // 9 mins at 10 txns/min
    )
  ).protocols(httpProtocol)
}