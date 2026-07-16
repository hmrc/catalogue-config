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

package uk.gov.hmrc.catalogueconfig.controllers

import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsObject, JsValue}
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.http.UpstreamErrorResponse
import uk.gov.hmrc.internalauth.client.test.{BackendAuthComponentsStub, StubBehaviour}
import uk.gov.hmrc.internalauth.client.{BackendAuthComponents, IAAction, Predicate, Resource, ResourceLocation, ResourceType, Retrieval, ~}

import scala.concurrent.{ExecutionContext, Future}

class MenuControllerSpec
  extends AnyWordSpec
    with Matchers
    with OptionValues
    with ScalaFutures
    with GuiceOneAppPerSuite:

  private def retrievalResult(
    createUserResources: Set[Resource],
    manageUserResources: Set[Resource]
  ): Set[Resource] ~ Set[Resource] =
    new ~(createUserResources, manageUserResources)

  private val emptyActions: Set[Resource] ~ Set[Resource] =
    retrievalResult(Set.empty, Set.empty)

  private val createOnlyActions: Set[Resource] ~ Set[Resource] =
    retrievalResult(
      Set(Resource(ResourceType("catalogue-frontend"), ResourceLocation("teams/*"))),
      Set.empty
    )

  private val manageOnlyActions: Set[Resource] ~ Set[Resource] =
    retrievalResult(
      Set.empty,
      Set(Resource(ResourceType("catalogue-frontend"), ResourceLocation("teams/*")))
    )

  private val createAndManageActions: Set[Resource] ~ Set[Resource] =
    retrievalResult(
      Set(Resource(ResourceType("catalogue-frontend"), ResourceLocation("teams/*"))),
      Set(Resource(ResourceType("catalogue-frontend"), ResourceLocation("teams/*")))
    )

  override def fakeApplication(): Application =
    given ControllerComponents = stubControllerComponents()
    given ExecutionContext = scala.concurrent.ExecutionContext.global

    val authStub: BackendAuthComponents =
      BackendAuthComponentsStub(new StubBehaviour:
        override def stubAuth[R](predicate: Option[Predicate], retrieval: Retrieval[R]): Future[R] =
          MenuControllerSpec.AuthStubState.record(retrieval)
          MenuControllerSpec.AuthStubState.nextResult.asInstanceOf[Future[R]]
      )

    GuiceApplicationBuilder()
      .overrides(
        bind[BackendAuthComponents].toInstance(authStub)
      )
      .build()

  "GET /catalogue-config/menu-bar/menu" should {
    "return 200 with top-level users link and no users dropdown when user has neither action" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(emptyActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      status(result) shouldBe OK
      usersTopLevelHref(contentAsJson(result)) shouldBe Some("/users")
      usersDropdown(contentAsJson(result)) shouldBe None
    }

    "return create-user and create-service-user in users dropdown when user has CREATE_USER only" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(createOnlyActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      status(result) shouldBe OK
      usersTopLevelHref(contentAsJson(result)) shouldBe Some("/users")
      usersDropdownItemIds(contentAsJson(result)) shouldBe Seq("create-user", "create-service-user")
    }

    "return only offboard-users in users dropdown when user has MANAGE_USER only" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(manageOnlyActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      status(result) shouldBe OK
      usersTopLevelHref(contentAsJson(result)) shouldBe Some("/users")
      usersDropdownItemIds(contentAsJson(result)) shouldBe Seq("offboard-users")
    }

    "return all users dropdown items when user has both actions" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(createAndManageActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      status(result) shouldBe OK
      usersTopLevelHref(contentAsJson(result)) shouldBe Some("/users")
      usersDropdownItemIds(contentAsJson(result)) shouldBe Seq("create-user", "create-service-user", "offboard-users")
    }

    "query both CREATE_USER and MANAGE_USER retrievals for catalogue-frontend" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(createAndManageActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      status(result) shouldBe OK

      MenuControllerSpec.AuthStubState.capturedRetrieval shouldBe Some(
        Retrieval.locations(
          resourceType = Some(ResourceType("catalogue-frontend")),
          action       = Some(IAAction("CREATE_USER"))
        ) ~
          Retrieval.locations(
            resourceType = Some(ResourceType("catalogue-frontend")),
            action       = Some(IAAction("MANAGE_USER"))
          )
      )
    }

    "not grant users dropdown from legacy or action role query parameter values when actions retrieval is empty" in {
      Seq("CREATE_USER", "MANAGE_USER", "CAN_CREATE_USERS", "CAN_MANAGE_USERS").foreach { role =>
        MenuControllerSpec.AuthStubState.reset(Future.successful(emptyActions))

        val result = route(
          app,
          FakeRequest(GET, s"/catalogue-config/menu-bar/menu?role=$role").withHeaders("Authorization" -> "Bearer test-token")
        ).value

        status(result) shouldBe OK
        usersDropdown(contentAsJson(result)) shouldBe None
      }
    }

    "return 401 for missing credentials" in {
      MenuControllerSpec.AuthStubState.reset(Future.successful(createAndManageActions))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu")
      ).value

      val exception = result.failed.futureValue
      exception shouldBe a[UpstreamErrorResponse]
      exception match
        case e: UpstreamErrorResponse => e.statusCode shouldBe UNAUTHORIZED
        case _                        => fail("Expected UpstreamErrorResponse")
    }

    "return 401 when Internal Auth rejects credentials" in {
      MenuControllerSpec.AuthStubState.reset(Future.failed(UpstreamErrorResponse("Unauthorized", UNAUTHORIZED)))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      val exception = result.failed.futureValue
      exception shouldBe a[UpstreamErrorResponse]
      exception match
        case e: UpstreamErrorResponse => e.statusCode shouldBe UNAUTHORIZED
        case _                        => fail("Expected UpstreamErrorResponse")
    }

    "propagate Internal Auth outage as a non-success failure" in {
      MenuControllerSpec.AuthStubState.reset(Future.failed(UpstreamErrorResponse("Internal Auth unavailable", INTERNAL_SERVER_ERROR)))

      val result = route(
        app,
        FakeRequest(GET, "/catalogue-config/menu-bar/menu").withHeaders("Authorization" -> "Bearer test-token")
      ).value

      val exception = result.failed.futureValue
      exception shouldBe a[UpstreamErrorResponse]
      exception match
        case e: UpstreamErrorResponse => e.statusCode shouldBe INTERNAL_SERVER_ERROR
        case _                        => fail("Expected UpstreamErrorResponse")
    }
  }

  private def usersTopLevelHref(json: JsValue): Option[String] =
    json
      .as[JsObject]
      .value("topLevelLinks")
      .as[Seq[JsObject]]
      .find(link => (link \ "id").as[String] == "users")
      .flatMap(link => (link \ "href").asOpt[String])

  private def usersDropdown(json: JsValue): Option[JsObject] =
    json
      .as[JsObject]
      .value("dropdowns")
      .as[Seq[JsObject]]
      .find(dropdown => (dropdown \ "id").as[String] == "users")

  private def usersDropdownItemIds(json: JsValue): Seq[String] =
    usersDropdown(json)
      .toSeq
      .flatMap(dropdown => (dropdown \ "items").as[Seq[JsObject]])
      .map(item => (item \ "id").as[String])

object MenuControllerSpec:
  private object AuthStubState:
    @volatile private var response: Future[Set[Resource] ~ Set[Resource]] = Future.successful(new ~(Set.empty[Resource], Set.empty[Resource]))
    @volatile var capturedRetrieval: Option[Any] = None

    def nextResult: Future[Set[Resource] ~ Set[Resource]] =
      response

    def reset(next: Future[Set[Resource] ~ Set[Resource]]): Unit =
      response = next
      capturedRetrieval = None

    def record(retrieval: Any): Unit =
      capturedRetrieval = Some(retrieval)
