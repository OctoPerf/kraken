package com.octoperf.tutorials.four

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PetStoreSimulation2 extends Simulation {

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
			
			
	val searchTerms = csv("four/search.csv").random

	val searchRequest = http("Search ${term}")
			.post("/actions/Catalog.action")
			.formParam("keyword", "${term}")
			.formParam("searchProducts", "Search")
			.check(regex("""productId=([^"]*)""").findRandom.saveAs("productId"))
	
  val productRequest = http("Product ${productId}")
      .get("/actions/Catalog.action")
      .queryParam("viewProduct", "")
      .queryParam("productId", "${productId}")
      .check(regex("""itemId=([^"]*)""").findRandom.saveAs("itemId"))

  val addItemToCartRequest = http("Add item ${itemId} to cart")
			.get("/actions/Cart.action")
      .queryParam("addItemToCart", "")
      .queryParam("workingItemId", "${itemId}")
  
  val newOrderFromRequest = http("New Order Form")
			.get("/actions/Order.action")
			.queryParam("newOrderForm", "")

  val orderRequest = http("Order")
			.post("/actions/Order.action")
			.formParam("order.cardType", "Visa")
			.formParam("order.creditCard", "999 9999 9999 9999")
			.formParam("order.expiryDate", "12/03")
			.formParam("order.billToFirstName", "ABC")
			.formParam("order.billToLastName", "XYX")
			.formParam("order.billAddress1", "901 San Antonio Road")
			.formParam("order.billAddress2", "MS UCUP02-206")
			.formParam("order.billCity", "Palo Alto")
			.formParam("order.billState", "CA")
			.formParam("order.billZip", "94303")
			.formParam("order.billCountry", "USA")
			.formParam("newOrder", "Continue")

  val confirmOrder = http("Confirm Order")
			.get("/actions/Order.action")
			.queryParam("newOrder", "")
			.queryParam("confirmed", "true")

	val scn = scenario("PetStoreSimulation")
      .exec(signonFormRequest)
      .feed(credentials)
      .exec(loginRequest)
      .feed(searchTerms)
      .exec(searchRequest)
      .exec(productRequest)
      .exec(addItemToCartRequest)
      .exec(newOrderFromRequest)
      .exec(orderRequest)
      .exec(confirmOrder)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}