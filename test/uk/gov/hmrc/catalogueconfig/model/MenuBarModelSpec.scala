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
import play.api.libs.json.Json
import uk.gov.hmrc.catalogueconfig.menu.NavMenuService
import uk.gov.hmrc.catalogueconfig.menu.NavMenuService.{createServiceUser, createUser, offboardUsers}

class MenuBarModelSpec extends
  AnyWordSpec
  with Matchers:

  "MenuBarModel JSON representation" can{
    "deserialize representation of menu elements" in {
      val json = Json.parse(
        """{
          |  "brand": {
          |    "id": "brand",
          |    "name": "MDTP",
          |    "href": "/",
          |    "external": false,
          |    "_type": "TopMenu"
          |  },
          |  "topLevelLinks": [],
          |  "dropdowns": [
          |    {
          |      "id": "docs",
          |      "name": "Docs",
          |      "href": null,
          |      "items": [
          |        {
          |          "name": "Docs Home",
          |          "id": "docs-home",
          |          "href": "/docs",
          |          "external": false,
          |          "_type": "Page"
          |        },
          |        {
          |          "_type": "DropdownSeparator"
          |        },
          |        {
          |          "name": "Blog",
          |          "id": "docs-blog",
          |          "href": "/docs/blog",
          |          "external": false,
          |          "_type": "Page"
          |        }
          |      ],
          |      "dropDownRole": []
          |    }
          |  ]
          |}""".stripMargin
        )

      val menu = json.as[BannerMenu]
      menu.dropdowns.head.items shouldBe Seq(
        Page("Docs Home", "docs-home", "/docs"),
        DropdownSeparator,
        Page("Blog", "docs-blog", "/docs/blog")
        )
    }

    "serialize representation of menu elements" in {
      val menu = BannerMenu(
        brand = TopMenu("MDTP", "brand", "/"),
        topLevelLinks = Seq(
          TopMenu("Users", "users", "/users"),
          TopMenu("Teams", "teams", "/teams"),
          TopMenu("Repositories", "repositories", "/repositories"),
          TopMenu("Deployments", "deployments", None)
        ),
        dropdowns = Seq(
          MenuDropdown(
            id = "docs",
            name = "Docs",
            href = None,
            items = Seq(
              Page("Docs Home", "docs-home", "/docs"),
              DropdownSeparator,
              Page("Blog", "docs-blog", "/docs/blog")
            ),
            dropDownRole = Seq.empty
          ),
          MenuDropdown(
            id = "users",
            name = "Users",
            href = Some("/users"),
            items = Seq(
                Page("Create a User", "create-user", "/create-user"),
                Page("Create a Service User", "create-service-user", "/create-service-user"),
                Page("Offboard Users", "offboard-users", "/offboard-users")
              )
            )
        )
      )

      val expectedJson = """{
        |  "brand": {
        |    "name": "MDTP",
        |    "id": "brand",
        |    "href": "/",
        |    "external": false,
        |    "_type": "TopMenu"
        |  },
        |  "topLevelLinks": [
        |    {
        |      "name": "Users",
        |      "id": "users",
        |      "href": "/users",
        |      "external": false,
        |      "_type": "TopMenu"
        |    },
        |    {
        |      "name": "Teams",
        |      "id": "teams",
        |      "href": "/teams",
        |      "external": false,
        |      "_type": "TopMenu"
        |    },
        |    {
        |      "name": "Repositories",
        |      "id": "repositories",
        |      "href": "/repositories",
        |      "external": false,
        |      "_type": "TopMenu"
        |    },
        |    {
        |      "name": "Deployments",
        |      "id": "deployments",
        |      "external": false,
        |      "_type": "TopMenu"
        |    }
        |  ],
        |  "dropdowns": [
        |    {
        |      "id": "docs",
        |      "name": "Docs",
        |      "items": [
        |        {
        |          "name": "Docs Home",
        |          "id": "docs-home",
        |          "href": "/docs",
        |          "external": false,
        |          "_type": "Page"
        |        },
        |        {
        |          "_type": "DropdownSeparator"
        |        },
        |        {
        |          "name": "Blog",
        |          "id": "docs-blog",
        |          "href": "/docs/blog",
        |          "external": false,
        |          "_type": "Page"
        |        }
        |      ],
        |      "dropDownRole": []
        |    },
        |    {
        |      "id": "users",
        |      "name": "Users",
        |      "href": "/users",
        |      "items": [
        |       {
        |          "name": "Create a User",
        |          "id": "create-user",
        |          "href": "/create-user",
        |          "external": false,
        |                    "_type": "Page"
        |        },
        |        {
        |          "name": "Create a Service User",
        |          "id": "create-service-user",
        |          "href": "/create-service-user",
        |          "external": false,
        |                    "_type": "Page"
        |        },
        |        {
        |          "name": "Offboard Users",
        |          "id": "offboard-users",
        |          "href": "/offboard-users",
        |          "external": false,
        |                    "_type": "Page"
        |        }
        |      ],
        |      "dropDownRole": []
        |    }
        |  ]
        |}""".stripMargin

      val actualJson = Json.toJson(menu)
      actualJson shouldBe Json.parse(expectedJson)
    }
  }
