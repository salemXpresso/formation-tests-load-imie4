/**
 * Copyright 2011-2018 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {
  /*
  val t_rampUp = Integer.getInteger("ramp-up", 1).toInt

  val httpConf = http
    .baseURL("http://172.16.35.18:8080") // Here is the root for all relative URLs
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
        .repeat(50){
      .get("/load-test/loadme"))
      .pause(100) // Note that Gatling has recorded real time pauses
    }

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))*/
  
  
  //val t_iterations = Integer.getInteger("iterations", 100).toInt
  val t_concurrency = Integer.getInteger("concurrency", 400).toInt
  val t_rampUp = Integer.getInteger("ramp-up", 60).toInt
  val t_holdFor = Integer.getInteger("hold-for", 60).toInt
  val t_throughput = Integer.getInteger("throughput", 2000).toInt
  val httpConf = http.baseURL("http://172.16.35.18:8080")
 
  // 'forever' means each thread will execute scenario until
  // duration limit is reached
  val loopScenario = scenario("Loop Scenario").forever() {
    exec(http("index").get("/load-test/loadme")).pause(100)
  }
 
  // if you want to set an iteration limit (instead of using duration limit),
  // you can use the following scenario
 /* val iterationScenario = scenario("Iteration Scenario").repeat(t_iterations) {
    exec(http("index").get("/"))
  }*/
 
  val execution = loopScenario
    .inject(rampUsers(t_concurrency) over (60 seconds))
    .protocols(httpConf)
 
  setUp(execution).
    throttle(jumpToRps(t_throughput), holdFor(t_holdFor)).
    maxDuration(t_rampUp + t_holdFor)
  
}
