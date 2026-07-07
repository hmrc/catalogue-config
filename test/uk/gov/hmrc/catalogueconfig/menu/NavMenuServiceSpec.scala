package uk.gov.hmrc.catalogueconfig.menu

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NavMenuServiceSpec
  extends AnyWordSpec
    with Matchers:

  private val service = new NavMenuService()

  "NavMenuService.buildMenu" should {

    "build the brand field with correct structure" in {
      val menu = service.buildMenu()

      menu.brand.name shouldBe "MDTP"
      menu.brand.id shouldBe "mdtp"
      menu.brand.description shouldBe "MDTP"
      menu.brand.href shouldBe Some("/")
    }

    "include all 8 top-level menu items" in {
      val menu = service.buildMenu()

      menu.topLevelLinks.length shouldBe 8
    }

    "include top-level menu 'Users' with correct structure" in {
      val menu = service.buildMenu()
      val users = menu.topLevelLinks.find(_.id == "users")

      users should not be empty
      users.get.name shouldBe "Users"
      users.get.description shouldBe "View and manage users"
      users.get.href shouldBe Some("/users")
    }

    "include top-level menu 'Teams' with correct structure" in {
      val menu = service.buildMenu()
      val teams = menu.topLevelLinks.find(_.id == "teams")

      teams should not be empty
      teams.get.name shouldBe "Teams"
      teams.get.description shouldBe "View and manage teams"
      teams.get.href shouldBe Some("/teams")
    }

    "include top-level menu 'Repositories' with correct structure" in {
      val menu = service.buildMenu()
      val repositories = menu.topLevelLinks.find(_.id == "repositories")

      repositories should not be empty
      repositories.get.name shouldBe "Repositories"
      repositories.get.description shouldBe "View and manage repositories"
      repositories.get.href shouldBe Some("/repositories")
    }

    "include top-level menu 'Deployments' with correct structure" in {
      val menu = service.buildMenu()
      val deployments = menu.topLevelLinks.find(_.id == "deployments")

      deployments should not be empty
      deployments.get.name shouldBe "Deployments"
      deployments.get.description shouldBe "View and manage deployments"
      deployments.get.href shouldBe None
    }

    "include top-level menu 'Shuttering' with correct structure" in {
      val menu = service.buildMenu()
      val shuttering = menu.topLevelLinks.find(_.id == "shuttering")

      shuttering should not be empty
      shuttering.get.name shouldBe "Shuttering"
      shuttering.get.description shouldBe "View and manage shuttering"
      shuttering.get.href shouldBe None
    }

    "include top-level menu 'Health' with correct structure" in {
      val menu = service.buildMenu()
      val health = menu.topLevelLinks.find(_.id == "health")

      health should not be empty
      health.get.name shouldBe "Health"
      health.get.description shouldBe "View and manage health"
      health.get.href shouldBe None
    }

    "include top-level menu 'Explore' with correct structure" in {
      val menu = service.buildMenu()
      val explore = menu.topLevelLinks.find(_.id == "explore")

      explore should not be empty
      explore.get.name shouldBe "Explore"
      explore.get.description shouldBe "Explore services"
      explore.get.href shouldBe None
    }

    "include top-level menu 'Docs' with correct structure" in {
      val menu = service.buildMenu()
      val docs = menu.topLevelLinks.find(_.id == "docs")

      docs should not be empty
      docs.get.name shouldBe "Docs"
      docs.get.description shouldBe "View documentation"
      docs.get.href shouldBe None
    }

    "ensure all top-level links with href follow the /{id} pattern" in {
      val menu = service.buildMenu()

      menu.topLevelLinks
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

