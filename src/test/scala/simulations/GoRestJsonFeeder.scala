package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class GoRestJsonFeeder extends Simulation {
  //httpConf
  val httpCof = http.baseUrl(url = "https://gorest.co.in")
    .header( name="Authorization",value = "Bearer fcedb3b3329cdb63fabb0de38a2b9c9f12eadf6f3e8dbb7572514f9f0273c5f4")

  val jsonFeeder = jsonFile("./src/test/resources/data/createUser.json").queue

  def createUser(): ChainBuilder = {
    repeat(4){
      feed(jsonFeeder)
        .exec(http("create multiple user request")
          .post("/public-api/users")
          .body(StringBody("""{ "name":"${name}","gender":"${gender}","email":"${email}","status":"${status}" }""")).asJson
          .header("content-type",value = "application/json")
          .check(status.in(200 to 210)))
    }
  }
  val scn= scenario("JSON feeding test").exec(createUser())

  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpCof)


}
