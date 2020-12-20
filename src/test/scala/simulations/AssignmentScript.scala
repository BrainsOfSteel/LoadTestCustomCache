package simulations

import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter
import java.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random.javaRandomToRandom


class AssignmentScript extends Simulation{
  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  val rnd = new Random()
  var idNumbers = (11 to 2000).iterator

  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  def randomString(length : Int)={
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate: LocalDate, random: Random): String={
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  def getAllVideoGame()={
    exec(http("Get all videogames")
    .get("videogames")
    .check(status.is(200)))
  }

  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(5)),
    "releaseDate" -> getRandomDate(now, rnd),
    "reviewScore" -> rnd.nextInt(100),
    "category" -> ("Category-" + randomString(6)),
    "rating" -> ("Rating-" + randomString(4))
  ))

  def createNewGame()={
    feed(customFeeder).
      exec(http("Post New Game")
        .post("videogames")
        .body(ElFileBody("bodies/NewGameTemplate.json")).asJson //template file goes in gating/resources/bodies
        .check(status.is(200)))
  }

  def getLastPostedGame()={
    exec(http("Last Posted Game")
    .get("videogames/${gameId}")
      .check(status.is(200)))
  }

  def deleteLastPostedGame()={
    exec(http("Delete Last Game")
    .delete("videogames/${gameId}")
      .check(status.is(200)))
  }

  val scn = scenario("User journey for assignment")
    .forever() {
      exec(getAllVideoGame())
        .pause(2)
        .exec(createNewGame())
        .pause(2)
        .exec(getLastPostedGame())
        .pause(2)
        .exec(deleteLastPostedGame())
    }

  setUp(scn.inject(atOnceUsers(10),
    rampUsers(10).during(2.seconds))
  ).protocols(httpConf)
    .maxDuration(20.seconds)
}
