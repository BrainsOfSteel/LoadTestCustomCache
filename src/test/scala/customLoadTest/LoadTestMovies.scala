package customLoadTest

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class LoadTestMovies extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8080/movies/")
    .header("Accept", "application/json")


  val scn = scenario("basic movie load test")
    .exec(http("get movies data").get("getAllMovies"))

  setUp(
//    scn.inject(atOnceUsers(100),
//      constantUsersPerSec(200).during(5.seconds))
    scn.inject(constantConcurrentUsers(1000).during(10.seconds))
  ).protocols(httpConf)
}
