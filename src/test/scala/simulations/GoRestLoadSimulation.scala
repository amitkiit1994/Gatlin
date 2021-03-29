package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class GoRestLoadSimulation extends Simulation {
  val httpCof = http.baseUrl(url = "https://gorest.co.in")
    .header( name="Authorization",value = "Bearer fcedb3b3329cdb63fabb0de38a2b9c9f12eadf6f3e8dbb7572514f9f0273c5f4")

  //chainingloop
  def getAllUser(): ChainBuilder={
    repeat(1){
        exec(http("get multiple user request")
          .get("/public-api/users")
          .check(status.in(200 to 210)))
    }
  }

  val scn= scenario("Ramp test").exec(getAllUser())

  setUp(scn.inject(
    nothingFor(1),
    atOnceUsers(users = 5),
    rampUsers(10) during(5)
  )).protocols(httpCof)
}
