/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.catalogueconfig

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HttpReads.Implicits.readRaw
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.internalauth.client.test.{BackendAuthComponentsStub, StubBehaviour}
import uk.gov.hmrc.internalauth.client.{BackendAuthComponents, Predicate, Retrieval}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MenuEndpointAuthIntegrationSpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with GuiceOneServerPerSuite:

  private val httpClient = app.injector.instanceOf[HttpClientV2]
  private val baseUrl = s"http://localhost:$port"

  override def fakeApplication(): Application =
    given ControllerComponents = stubControllerComponents()

    val authStub: BackendAuthComponents =
      BackendAuthComponentsStub(new StubBehaviour:
        override def stubAuth[R](predicate: Option[Predicate], retrieval: Retrieval[R]): Future[R] =
          Future.failed(new RuntimeException("Auth stub should not be called for missing credentials test"))
      )

    GuiceApplicationBuilder()
      .overrides(
        bind[BackendAuthComponents].toInstance(authStub)
      )
      .build()

  "menu endpoint authentication" should:
    "return HTTP 401 when authorization credentials are missing" in:
      val response =
        httpClient
          .get(url"$baseUrl/catalogue-config/menu-bar/menu")(HeaderCarrier())
          .execute()
          .futureValue

      response.status shouldBe 401
