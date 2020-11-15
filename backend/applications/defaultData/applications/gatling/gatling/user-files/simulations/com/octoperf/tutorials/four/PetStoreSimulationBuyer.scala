package com.octoperf.tutorials.four

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object PetStoreSimulationBuyer {

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

	val scn = scenario("PetStoreSimulationBuyer")
      .exec(signonFormRequest)
      .pause(500 milliseconds)
      .feed(credentials)
      .exec(loginRequest)
      .pause(500 milliseconds)
      .feed(searchTerms)
      .exec(searchRequest)
      .pause(500 milliseconds)
      .exec(productRequest)
      .pause(500 milliseconds)
      .exec(addItemToCartRequest)
      .pause(500 milliseconds)
      .exec(newOrderFromRequest)
      .pause(500 milliseconds)
      .exec(orderRequest)
      .pause(500 milliseconds)
      .exec(confirmOrder)
}