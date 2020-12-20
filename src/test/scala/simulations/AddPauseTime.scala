package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class AddPauseTime extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", value = "application/xml")

  val scn = scenario("Video Game DB - 3 calls")
    .exec(http("Get all video games - 1st call")
    .get("videogames"))
    .pause(5)
    .exec(http("Get specific game").get("videogames/1"))
    .pause(1, 20)

    .exec(http("get all videogames").get("videogames"))
    .pause(3000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(100))
  ).protocols(httpConf)

}
