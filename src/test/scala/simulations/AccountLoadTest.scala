package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class AccountLoadTest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:4567")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val accountA = "firstNonTreasuryAccountID"  // Replace with actual ID
  val accountB = "secondNonTreasuryAccountID" // Replace with actual ID

  val transferScenario = scenario("Account Transfer Load Test")
    .exec(
      http("Transfer Money")
        .post("/accounts/transfer")
        .body(StringBody(
          s"""{
             "from": "$accountA",
             "to": "$accountB",
             "amount": "50"
          }""")).asJson
        .check(status.is(200))
    )

  setUp(
    transferScenario.inject(
      rampUsers(10) during (1.minute),  // 1 min ramp-up
      constantUsersPerSec(10.0 / 60) during (9.minutes) // 10 transactions per min for 9 min
    )
  ).protocols(httpProtocol)
}