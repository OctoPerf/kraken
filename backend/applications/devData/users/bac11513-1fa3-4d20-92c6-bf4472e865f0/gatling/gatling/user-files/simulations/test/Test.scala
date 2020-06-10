package test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Test extends Simulation {

	val httpProtocol = http
		.baseUrl("https://httpbin.org")
		.inferHtmlResources()
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")

	val headers_0 = Map("Origin" -> "https://httpbin.org")

	val headers_1 = Map(
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7,es;q=0.6",
		"Pragma" -> "no-cache",
		"accept" -> "application/json")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7,es;q=0.6",
		"Pragma" -> "no-cache",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7,es;q=0.6",
		"Pragma" -> "no-cache")

    val uri2 = "https://fonts.gstatic.com/s/titilliumweb/v7"

	val scn = scenario("Test")
		.exec(http("request_0")
			.get(uri2 + "/NaPecZTIAOhVxoMyOr9n_E7fdMPmDQ.woff2")
			.headers(headers_0))
		.pause(31)
		.exec(http("request_1")
			.get("/get")
			.headers(headers_1)
			.resources(http("request_2")
			.get(uri2 + "/NaPDcZTIAOhVxoMyOr9n_E7ffBzCGItzYw.woff2")
			.headers(headers_0)))
		.pause(20)
		.exec(http("request_3")
			.get("/get?test=abc")
			.headers(headers_3)
			.resources(http("request_4")
			.get("/favicon.ico")
			.headers(headers_4)
			.check(status.is(404))))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}