package com.octoperf.tutorials.two

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PetStoreSimulation0 extends Simulation {

    val httpProtocol = http
        .baseUrl("https://petstore.octoperf.com")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
        .inferHtmlResources(BlackList(), WhiteList("https://petstore.octoperf.com/.*"))

    val scn = scenario("PetStoreSimulation")
        .exec(http("Homepage").get("/actions/Catalog.action"))

    setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
