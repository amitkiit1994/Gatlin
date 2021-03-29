package simulations


import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestSimulation extends Simulation{

  //httpConf
  val httpCof = http.baseUrl(url = "https://regres.in")
    .header("Accept",value = "application/json")
    .header( name="content-type",value = "application/json" )

  //scenario
  val scn = scenario(scenarioName = "Get User")
    .exec(http(requestName = "Get User request")
      .get("/api/users/2")
      .check(status is 200))

  //setup
  setUp(scn.inject(atOnceUsers(users = 50))).protocols(httpCof)
}

