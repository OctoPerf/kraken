package com.octoperf.tutorials.four

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object PetStoreSimulationVisitor {

    val csvRecords = csv("four/categories.csv").readRecords

    val categoryRequest = http("Catalog ${categoryId}")
            .get("/actions/Catalog.action")
            .queryParam("viewCategory", "")
            .queryParam("categoryId", "${categoryId}")
            .check(regex("""productId=(.*)"""").findAll.saveAs("productIds"))

    val productRequest = http("Product ${productId}")
          .get("/actions/Catalog.action")
          .queryParam("viewProduct", "")
          .queryParam("productId", "${productId}")

    val scn = scenario("PetStoreSimulationVisitor")
        .exec(http("Homepage").get("/actions/Catalog.action"))
        .pause(500 milliseconds)
        .foreach(csvRecords, "category") {
          exec(flattenMapIntoAttributes("${category}"))
          .exec(categoryRequest)
          .pause(500 milliseconds)
          .during(10 seconds) {
            pace(1 seconds)
            .exec(session => {
              val productIds = session("productIds").as[List[Any]]
              val productIndex = util.Random.nextInt(productIds.size)
              session.set("productId", productIds(productIndex))
            })
            .exec(productRequest)
          }
        }
}
