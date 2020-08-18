package com.octoperf.tutorials.four

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PetStoreSimulation1 extends Simulation {

  val httpProtocol = http
      .baseUrl("https://petstore.octoperf.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
      // .inferHtmlResources(BlackList(), WhiteList("https://petstore.octoperf.com/.*"))

  val credentials = csv("four/credentials.csv").random

 val signonFormRequest = http("Signon Form")
      .get("/actions/Account.action")
			.queryParam("signonForm", "")

  val loginRequest = http("Login ${login}")
      .post("/actions/Account.action")
			.formParam("username", "${login}")
			.formParam("password", "${password}")
			.formParam("signon", "Login")
			.check(substring("Welcome ABC!").exists)

	val scn = scenario("PetStoreSimulation")
      .exec(signonFormRequest)
      .feed(credentials)
      .exec(loginRequest)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}