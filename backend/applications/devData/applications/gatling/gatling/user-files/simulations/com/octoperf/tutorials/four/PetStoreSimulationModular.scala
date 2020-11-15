package com.octoperf.tutorials.four

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import com.octoperf.tutorials.four.PetStoreSimulationBuyer._
import com.octoperf.tutorials.four.PetStoreSimulationVisitor._

class PetStoreSimulationModular extends Simulation {

    val httpProtocol = http
        .baseUrl("https://petstore.octoperf.com")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
        .inferHtmlResources(BlackList(), WhiteList("https://petstore.octoperf.com/.*"))

    
    setUp(PetStoreSimulationBuyer.scn.inject(constantConcurrentUsers(2) during(2 minutes)),
          PetStoreSimulationVisitor.scn.inject(constantConcurrentUsers(10) during(2 minutes))).protocols(httpProtocol)
}
