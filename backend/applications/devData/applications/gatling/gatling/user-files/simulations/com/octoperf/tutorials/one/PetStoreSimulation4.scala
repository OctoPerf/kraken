package com.octoperf.tutorials.one

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PetStoreSimulation4 extends Simulation {

    val httpProtocol = http
        .baseUrl("https://petstore.octoperf.com")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
        .doNotTrackHeader("1")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

	val headers_0 = Map(
		"accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"cache-control" -> "no-cache",
		"pragma" -> "no-cache",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "none",
		"sec-fetch-user" -> "?1",
		"upgrade-insecure-requests" -> "1")

	val headers_1 = Map(
		"accept" -> "text/css,*/*;q=0.1",
		"cache-control" -> "no-cache",
		"pragma" -> "no-cache",
		"sec-fetch-mode" -> "no-cors",
		"sec-fetch-site" -> "same-origin")

	val headers_2 = Map(
		"cache-control" -> "no-cache",
		"pragma" -> "no-cache",
		"sec-fetch-mode" -> "no-cors",
		"sec-fetch-site" -> "same-origin")



	val scn = scenario("PetStoreSimulation")
		.exec(http("request_0")
			.get("/actions/Catalog.action")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/css/jpetstore.css")
			.headers(headers_1),
            http("request_2")
			.get("/images/logo-topbar.gif")
			.headers(headers_2),
            http("request_3")
			.get("/images/cart.gif")
			.headers(headers_2),
            http("request_4")
			.get("/images/separator.gif")
			.headers(headers_2),
            http("request_5")
			.get("/images/sm_fish.gif")
			.headers(headers_2),
            http("request_6")
			.get("/images/sm_dogs.gif")
			.headers(headers_2),
            http("request_7")
			.get("/images/sm_reptiles.gif")
			.headers(headers_2),
            http("request_8")
			.get("/images/sm_cats.gif")
			.headers(headers_2),
            http("request_9")
			.get("/images/sm_birds.gif")
			.headers(headers_2),
            http("request_10")
			.get("/images/fish_icon.gif")
			.headers(headers_2),
            http("request_11")
			.get("/images/dogs_icon.gif")
			.headers(headers_2),
            http("request_12")
			.get("/images/cats_icon.gif")
			.headers(headers_2),
            http("request_13")
			.get("/images/reptiles_icon.gif")
			.headers(headers_2),
            http("request_14")
			.get("/images/birds_icon.gif")
			.headers(headers_2),
            http("request_15")
			.get("/images/splash.gif")
			.headers(headers_2)))

    setUp(scn.inject(constantConcurrentUsers(100) during(3 minutes))).protocols(httpProtocol)
}
