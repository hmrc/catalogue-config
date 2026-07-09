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

import play.api.libs.json.{Format, Json, JsonConfiguration, JsonNaming}

sealed trait MenuLink {
  def name: String

  def id: String

  def href: Option[String]

  def external: Boolean
}

object MenuLink:
  given JsonConfiguration        = JsonConfiguration(
    typeNaming = JsonNaming(_.split("\\.").last)
    )
  given format: Format[MenuLink] = Json.format[MenuLink]

final case class BannerMenu(
  brand          :MenuLink,
  topLevelLinks  :Seq[MenuLink],
  dropdowns      :Seq[MenuDropdown]
)

object BannerMenu:
  given format: Format[BannerMenu] =
    Json.format[BannerMenu]

  val empty: BannerMenu =
    BannerMenu(
      brand = TopMenu("brand", "MDTP", Some("/")),
      topLevelLinks = Seq.empty,
      dropdowns = Seq.empty
      )

final case class MenuDropdown(
  id           :String,
  name         :String,
  href         :Option[String],
  items        :Seq[MenuLink],
  dropDownRole : Seq[Role] = Nil
)

object MenuDropdown {
  given format: Format[MenuDropdown] =
    Json.format[MenuDropdown]
}

final case class TopMenu(
  name        :String,
  id          :String,
  href        :Option[String],
  external    :Boolean = false
) extends MenuLink

object TopMenu:
  given format: Format[TopMenu]                                                   = Json.format[TopMenu]
  def apply(name: String, id: String, href: String): TopMenu =
    TopMenu(name, id, Some(href))

  def apply(name: String, id: String): TopMenu =
    TopMenu(name, id, None)

final case class Page(
  name        :String,
  id          :String,
  href        :Option[String],
  external    :Boolean = false
) extends MenuLink

object Page:
  given format: Format[Page] = Json.format[Page]

  def apply(name: String, id: String, href: String): Page =
    Page(name, id, Some(href))


