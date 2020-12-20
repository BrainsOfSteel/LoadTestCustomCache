package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class RampUserSimulation extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  def getAllVideoGames()={
    exec(
      http("getAll video games")
        .get("videogames")
        .check(status.is(200))
    )
  }

  def getSpecificGame()={
    exec(
      http("Get specific game")
        .get("videogames/2")
        .check(status.is(200))
    )
  }

  val scn = scenario("Constant Users Load Simulation")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(nothingFor(5.seconds),
      atOnceUsers(100),
      rampUsersPerSec(100).to(100) during (10.seconds)
  ).protocols(httpConf.inferHtmlResources()))
}
