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

import play.api.libs.json.{Format, Json}

sealed trait NavTarget {
  def name: String

  def id: String

  def description: String

  def href: Option[String]
}

final case class BannerMenu(
  brand         :TopMenu,
  topLevelLinks :Seq[TopMenu],
  dropdowns     :Seq[MenuDropdown]
)

object BannerMenu:
  given format: Format[BannerMenu] =
    Json.format[BannerMenu]

final case class TopMenu(
  name: String,
  id: String,
  description: String,
  href: Option[String]
) extends NavTarget

object TopMenu:
  given format: Format[TopMenu] = Json.format[TopMenu]
  def apply(name: String, id: String, description: String, href: String): TopMenu =
    TopMenu(name, id, description, Some(href))

  def apply(name: String, id: String, description: String): TopMenu =
    TopMenu(name, id, description, None)

final case class MenuDropdown(
  id     :String,
  name   :String,
  href   :Option[String],
  items  :Seq[MenuLink],
  dropDownRole: Seq[Role] = Nil
)

object MenuDropdown {
  given format: Format[MenuDropdown] =
    Json.format[MenuDropdown]
}

final case class MenuLink(
    id       :String,
    name     :String,
    href     :String,
    external :Boolean = false
)

object MenuLink:
  given format: Format[MenuLink] = Json.format




final case class Page(name: String, id: String, description: String, href: Option[String]) extends NavTarget

object Page:
  given format: Format[Page] = Json.format[Page]
  def apply(name: String, id: String, description: String, href: String): Page =
    Page(name, id, description, Some(href))

  def apply(name: String, id: String, description: String): Page =
    Page(name, id, description, None)


