package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class GoRestLoop extends Simulation {

  //http Configure
  val httpCof = http.baseUrl(url = "https://regres.in")
    .header("Accept",value = "application/json")
    .header( name="content-type",value = "application/json" )

  //loooping a request 10 time
  def addAUser(): ChainBuilder = {
    repeat(10) {
      exec(http(requestName = "Add a User")
        .post("/api/users")
        .body(RawFileBody(filePath = "./src/test/resources/requestPayloads/AddUser.json"))
        .header(name="content-type",value = "application/json")
        .check(status.in(expected = 200 to 210)))
    }
  }

  def getAUser(): ChainBuilder = {
    repeat(10) {
      exec(http(requestName = "Get User request")
        .get("/api/users/2")
        .check(status.is(expected = 200)))
    }
  }

  //injecting the loop request in scenarios
  val scn = scenario("User requesting through loop")
    .exec(addAUser())
    .pause(2)
    .exec(getAUser())

  //setup
  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpCof)

}
