import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstTest extends Simulation{
  //1. Http Conf
  //2. Scenario Definition
  //3. Load Scenario


  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", value = "application/json")

  //2 Scenario
  val scn = scenario("My First test")
    .exec(http("Get all Games").get("videogames"))

  //3 Load scenario
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
