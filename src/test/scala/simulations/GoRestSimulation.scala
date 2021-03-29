package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class GoRestSimulation extends Simulation {
  //httpConf
  val httpCof = http.baseUrl(url = "https://gorest.co.in")
    .header( name="Authorization",value = "Bearer fcedb3b3329cdb63fabb0de38a2b9c9f12eadf6f3e8dbb7572514f9f0273c5f4")
  //scenario
  val scn = scenario("End to End User Flow")
    .exec(http("Create a User")
      .post("/public-api/users")
      .body(RawFileBody("./src/test/resources/requestPayloads/goRestPayloads/AddUser.json"))
      .header("content-type",value = "application/json")
      .check(jsonPath("$.data.id").saveAs("userId"))
      .check(status.in(200 to 210)))
    .pause(2)
    .exec(http("Retrieve the user")
      .get("/public-api/users/${userId}")
      .check(status.in(200 to 210))
      .check(jsonPath("$.data.name").is("Amit Kumar Das"))
      .check(status.in(200 to 210)))
    .pause(duration = 2)
    .exec(http("Update the created user")
      .put("/public-api/users/${userId}")
      .body(RawFileBody("./src/test/resources/requestPayloads/goRestPayloads/UpdateUser.json"))
      .header("content-type",value = "application/json")
      .check(jsonPath("$.data.name").is("Kabir Advaitya"))
      .check(status.in(200 to 210)))
    .pause(2)
    .exec(http("Delete the user")
      .delete("/public-api/users/${userId}")
      .check(status.in(200 to 210)))
  //setup
  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpCof)
}
