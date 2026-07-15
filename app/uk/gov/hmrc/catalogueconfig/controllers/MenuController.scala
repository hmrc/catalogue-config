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

import play.api.libs.json.Json
import play.api.mvc.*
import uk.gov.hmrc.catalogueconfig.UserContext
import uk.gov.hmrc.catalogueconfig.menu.{NavMenuService, SearchService}

import javax.inject.{Inject, Singleton}

@Singleton
class MenuController @Inject()(
    val controllerComponents : ControllerComponents,
    menuService              : NavMenuService,
    searchService            : SearchService
) extends BaseController:

  def menu(): Action[AnyContent] = Action: request =>
    val userContext = request.getQueryString("role")
      .map(roleId => UserContext.fromRoleIdentifier(roleId))
      .getOrElse(UserContext.empty)
    Ok(Json.toJson(menuService.buildMenu(userContext)))


  def search(): Action[AnyContent] = Action:
    // TODO: Consider whether search belongs in this controller
    Ok(Json.toJson(searchService.fullSearchIndex))


