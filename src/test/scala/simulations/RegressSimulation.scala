package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class RegressSimulation extends Simulation {
  //httpConf
  val httpCof = http.baseUrl(url = "https://regres.in")
    .header("Accept",value = "application/json")
    .header( name="content-type",value = "application/json" )

  //scenario
  val scn = scenario(scenarioName = "End to End User Flow")
    .exec(http(requestName = "Get User request")
      .get("/api/users/2")
      .check(status.is(expected = 200)))
    .pause(duration = 2)
    .exec(http(requestName = "Add a User")
      .post("/api/users")
      .body(RawFileBody(filePath = "./src/test/resources/requestPayloads/AddUser.json"))
      .header(name="content-type",value = "application/json")
      .check(status.in(expected = 200 to 210)))
    .pause(duration = 2)
    .exec(http(requestName = "Update a User")
      .put(url = "/api/users/2")
      .body(RawFileBody(filePath = "./src/test/resources/requestPayloads/UpdateUser.json"))
      .header(name="content-type",value = "application/json")
      .check(status.in(expected = 200 to 210)))
    .pause(duration = 2)
    .exec(http(requestName = "Delete a User")
      .delete(url = "/api/users/2")
      .check(status.in(200 to 204)))


  //setup
  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpCof)
}
