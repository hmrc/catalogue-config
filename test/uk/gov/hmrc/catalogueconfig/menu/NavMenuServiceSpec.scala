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

package uk.gov.hmrc.catalogueconfig.menu

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NavMenuServiceSpec
  extends AnyWordSpec
    with Matchers:

  private val service = new NavMenuService()

  "NavMenuService.buildMenu" should {

    val topMenus = NavMenuService.topLevelMenus
    "build the brand field with correct structure" in {
      val menu = service.buildMenu()

      menu.brand.name shouldBe "MDTP"
      menu.brand.id shouldBe "mdtp"
      menu.brand.href should contain ("/")
    }

    "include all 8 top-level menu items" in {
      val menu = service.buildMenu()

      menu.topLevelLinks.length shouldBe 8
    }

    "include top-level menu 'Users' with correct structure" in {
      val users = topMenus.find(_.id == "users")

      users should not be empty
      users.get.name shouldBe "Users"
      users.get.description shouldBe "View and manage users"
      users.get.href shouldBe Some("/users")
    }

    "include top-level menu 'Teams' with correct structure" in {
      val teams = topMenus.find(_.id == "teams")

      teams should not be empty
      teams.get.name shouldBe "Teams"
      teams.get.description shouldBe "View and manage teams"
      teams.get.href shouldBe Some("/teams")
    }

    "include top-level menu 'Repositories' with correct structure" in {
      val repositories = topMenus.find(_.id == "repositories")

      repositories should not be empty
      repositories.get.name shouldBe "Repositories"
      repositories.get.description shouldBe "View and manage repositories"
      repositories.get.href shouldBe Some("/repositories")
    }

    "include top-level menu 'Deployments' with correct structure" in {
      val deployments = topMenus.find(_.id == "deployments")

      deployments should not be empty
      deployments.get.name shouldBe "Deployments"
      deployments.get.description shouldBe "View and manage deployments"
      deployments.get.href shouldBe None
    }

    "include top-level menu 'Shuttering' with correct structure" in {
      val shuttering = topMenus.find(_.id == "shuttering")

      shuttering should not be empty
      shuttering.get.name shouldBe "Shuttering"
      shuttering.get.description shouldBe "View and manage shuttering"
      shuttering.get.href shouldBe None
    }

    "include top-level menu 'Health' with correct structure" in {
      val health = topMenus.find(_.id == "health")

      health should not be empty
      health.get.name shouldBe "Health"
      health.get.description shouldBe "View and manage health"
      health.get.href shouldBe None
    }

    "include top-level menu 'Explore' with correct structure" in {
      val explore = topMenus.find(_.id == "explore")

      explore should not be empty
      explore.get.name shouldBe "Explore"
      explore.get.description shouldBe "Explore services"
      explore.get.href shouldBe None
    }

    "include top-level menu 'Docs' with correct structure" in {
      val docs = topMenus.find(_.id == "docs")

      docs should not be empty
      docs.get.name shouldBe "Docs"
      docs.get.description shouldBe "View documentation"
      docs.get.href shouldBe None
    }

    "ensure all top-level links with href follow the /{id} pattern" in {
      NavMenuService.topLevelMenus
        .filter(_.href.isDefined)
        .foreach { link =>
          val hrefValue = link.href.get
          hrefValue should startWith("/")
          // For top-level links with href, verify they match /{id} pattern
          hrefValue should (equal(s"/${link.id}") or equal("/"))
        }
    }

    "ensure all top-level links have matching id field" in {
      val menu = service.buildMenu()

      val expectedIds = Set("users", "teams", "repositories", "deployments", "shuttering", "health", "explore", "docs")
      val actualIds = menu.topLevelLinks.map(_.id).toSet

      actualIds shouldBe expectedIds
    }

    // Note: Dropdown items testing is deferred to a future test phase
  }

