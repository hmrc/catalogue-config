package uk.gov.hmrc.catalogueconfig.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class MenuBarModelSpec extends
  AnyWordSpec
  with Matchers:

  "MenuBarModel JSON representation" can{
    "deserialize representation of menu DropdownSeparator" in {
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
        DropdownSeparator(),
        Page("Blog", "docs-blog", "/docs/blog")
        )
    }
  }

