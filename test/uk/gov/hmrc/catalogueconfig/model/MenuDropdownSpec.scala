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

package uk.gov.hmrc.catalogueconfig.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.*

class MenuDropdownSpec extends AnyWordSpec with Matchers:

  "MenuDropdown JSON format" when {
    val dropdown = MenuDropdown(
      id = "users",
      name = "Users",
      href = Some("/users"),
      items = Seq(
        Page(id = "create-user", name = "Create User", href = Some("/create-user"))
        ),
      dropDownRole = Seq(Role.CanCreate, Role.CanManageUsers)
      )


    "roles are specified" can {
      val expectedJson: JsValue =
        Json.parse(
          """
            |{
            |  "id" : "users",
            |  "name" : "Users",
            |  "href" : "/users",
            |  "items" : [ {
            |    "name" : "Create User",
            |    "id" : "create-user",
            |    "href" : "/create-user",
            |    "external" : false,
            |    "_type" : "Page"
            |  } ],
            |  "dropDownRole" : [ "CAN_CREATE_USERS", "CAN_MANAGE_USERS" ]
            |}""".stripMargin
          )

      "serialize to JSON" in {
        Json.toJson(dropdown) shouldBe expectedJson
      }

      "deserialize from JSON" in {
        Json.fromJson[MenuDropdown](expectedJson) shouldBe JsSuccess(dropdown)
      }
    }
  }

