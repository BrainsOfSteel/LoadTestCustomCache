package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class CheckStatus extends Simulation{
  var httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept" ,"application/json")

  var scn = scenario("VideoGame DB- 3 calls")
    .exec(http("Get all video games - 1st call").get("videogames"))
    .pause(3)
    .exec(http("Get specific game").get("videogames/1").check(status.is(200)))
    .pause(1, 10)
    .exec(http("get all videogames").get("videogames"))
    .pause(3000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(100))
  ).protocols(httpConf)


}
