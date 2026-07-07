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
import org.scalatest.prop.TableDrivenPropertyChecks.*
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.*

class RoleSpec
  extends AnyWordSpec with Matchers:

  "Role JSON format" when {

    val roleCases = Table(
      ("role", "identifier"),
      (Role.CanCreate, "CAN_CREATE_USERS"),
      (Role.CanManageUsers, "CAN_MANAGE_USERS")
      )

    "deserializing from JSON" should {
      forAll(roleCases) { (role, identifier) =>
        s"write role $role to JSON identifier '$identifier''" in {
          Json.toJson[Role](role) shouldBe JsString(identifier)
        }
      }
    }

    "serializing from JSON" should {
      forAll(roleCases) { (role, identifier) =>
        s"convert JSON identifier '$identifier' string to role class $role" in {
          Json.fromJson[Role](JsString(identifier)) shouldBe JsSuccess(role)
        }
      }
    }

    "fail on unknown identifier" in {
      val result = Json.fromJson[Role](JsString("not-a-real-role"))
      result.isError shouldBe true
    }

    "fail when JSON is not a string" in {
      val result = Json.fromJson[Role](Json.obj("role" -> "can-create"))
      result.isError shouldBe true
    }
  }
