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

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.catalogueconfig.UserContext
import uk.gov.hmrc.catalogueconfig.model.Role

class NavMenuServiceSpec
  extends AnyWordSpec
    with Matchers
    with OptionValues:

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

      users.value.name shouldBe "Users"
      users.value.href should contain ("/users")
    }

    "include top-level menu 'Teams' with correct structure" in {
      val teams = topMenus.find(_.id == "teams")

      teams.value.name shouldBe "Teams"
      teams.value.href should contain ("/teams")
    }

    "include top-level menu 'Repositories' with correct structure" in {
      val repositories = topMenus.find(_.id == "repositories")

      repositories.value.name shouldBe "Repositories"
      repositories.value.href should contain ("/repositories")
    }

    "include top-level menu 'Deployments' with correct structure" in {
      val deployments = topMenus.find(_.id == "deployments")

      deployments.value.name shouldBe "Deployments"
      deployments.value.href shouldBe None
    }

    "include top-level menu 'Shuttering' with correct structure" in {
      val shuttering = topMenus.find(_.id == "shuttering")

      shuttering.value.name shouldBe "Shuttering"
      shuttering.value.href shouldBe None
    }

    "include top-level menu 'Health' with correct structure" in {
      val health = topMenus.find(_.id == "health")

      health.value.name shouldBe "Health"
      health.value.href shouldBe None
    }

    "include top-level menu 'Explore' with correct structure" in {
      val explore = topMenus.find(_.id == "explore")
      explore.value.name shouldBe "Explore"
      explore.value.href shouldBe None
    }

    "include top-level menu 'Docs' with correct structure" in {
      val docs = topMenus.find(_.id == "docs")

      docs should not be empty
      docs.value.name shouldBe "Docs"
      docs.value.href shouldBe None
    }

    "ensure all top-level links with href follow the /{id} pattern" in {
      NavMenuService.topLevelMenus
        .filter(_.href.isDefined)
        .foreach { link =>
          // For top-level links with href, verify they match /{id} pattern
          link.href.value shouldBe s"/${link.id}"
        }
    }

     "ensure all top-level links have matching id field" in {
       val menu = service.buildMenu()

       val expectedIds = Set("users", "teams", "repositories", "deployments", "shuttering", "health", "explore", "docs")
       val actualIds = menu.topLevelLinks.map(_.id).toSet

       actualIds shouldBe expectedIds
     }
   }

   "NavMenuService.buildMenu with role filtering" should {

     "include all dropdowns except those requiring roles when no role is provided" in {
       val menu = service.buildMenu(UserContext.empty)

       // Only the 'users' dropdown requires a role (CanManageUsers), so it's filtered out
       // Expected: 5 dropdowns (all except users out of 6 total)
       menu.dropdowns.length shouldBe 5
       menu.dropdowns.find(_.id == "users") shouldBe empty
     }

     "include users dropdown when user has CanManageUsers role" in {
       val userContext = UserContext(Set(Role.CanManageUsers))
       val menu = service.buildMenu(userContext)

       // With the required role, all 6 dropdowns should be included
       menu.dropdowns.length shouldBe 6
       val usersDropdown = menu.dropdowns.find(_.id == "users")
       usersDropdown should not be empty
       usersDropdown.value.dropDownRole should contain only Role.CanManageUsers
     }

     "exclude users dropdown when user does not have CanManageUsers role" in {
       val userContext = UserContext(Set(Role.CanCreate))
       val menu = service.buildMenu(userContext)


       // User has CanCreate but not CanManageUsers, so users dropdown is filtered out
       menu.dropdowns.length shouldBe 5
       menu.dropdowns.find(_.id == "users") shouldBe empty
     }

     "include dropdowns with no role requirement regardless of user role" in {
       val userContext = UserContext(Set(Role.CanCreate))
       val menu = service.buildMenu(userContext)

       // Deployments dropdown has no role requirement (empty list)
       val deploymentsDropdown = menu.dropdowns.find(_.id == "deployments")
       deploymentsDropdown should not be empty
       deploymentsDropdown.value.dropDownRole shouldBe empty
     }
   }

