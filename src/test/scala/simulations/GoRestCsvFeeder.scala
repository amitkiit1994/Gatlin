package simulations
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
class GoRestCsvFeeder extends Simulation {
  //httpConf
  val httpCof = http.baseUrl(url = "https://gorest.co.in")
    .header( name="Authorization",value = "Bearer fcedb3b3329cdb63fabb0de38a2b9c9f12eadf6f3e8dbb7572514f9f0273c5f4")
  //feedingcsv
  val csvFeederGetUsers = csv("./src/test/resources/data/getUser.csv").circular
  val csvFeederCreateUsers = csv("./src/test/resources/data/createUser.csv").queue

  //chainingloop
  def getAllUser(): ChainBuilder={
    repeat(17){
      feed(csvFeederGetUsers)
        .exec(http("get multiple user request")
          .get("/public-api/users/${userId}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200 to 210)))
    }
  }

  def createUser(): ChainBuilder = {
    repeat(5){
      feed(csvFeederCreateUsers)
      .exec(http("create multiple user request")
        .post("/public-api/users")
        .body(StringBody("""{ "name":"${name}","gender":"${gender}","email":"${email}","status":"${stat}" }""")).asJson
        .header("content-type",value = "application/json")
        .check(status.in(200 to 210)))
    }
  }

  val scn= scenario("CSV feeding test").exec(getAllUser()).pause(2).exec(createUser())

  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpCof)


}
