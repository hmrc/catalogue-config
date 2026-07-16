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
import uk.gov.hmrc.internalauth.client.{BackendAuthComponents, IAAction, Resource, ResourceType, Retrieval, ~}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MenuController @Inject()(
    val controllerComponents : ControllerComponents,
    menuService              : NavMenuService,
    searchService            : SearchService,
    auth                     : BackendAuthComponents
)(
    using ExecutionContext
) extends BaseController:

  private val menuResourceType: ResourceType =
    ResourceType("catalogue-frontend")

  private val menuUserActionsRetrieval: Retrieval[Set[Resource] ~ Set[Resource]] =
    Retrieval.locations(
      resourceType = Some(menuResourceType),
      action       = Some(IAAction("CREATE_USER"))
    ) ~
      Retrieval.locations(
        resourceType = Some(menuResourceType),
        action       = Some(IAAction("MANAGE_USER"))
      )

  def menu(): Action[AnyContent] =
    auth
      .authenticatedAction(
        retrieval = menuUserActionsRetrieval
      )
      .async: request =>
        val createUserResources ~ manageUserResources = request.retrieval
        val userContext =
          UserContext(
            canCreateUser = createUserResources.nonEmpty,
            canManageUser = manageUserResources.nonEmpty
          )

        Future.successful(Ok(Json.toJson(menuService.buildMenu(userContext))))

  def search(): Action[AnyContent] = Action:
    // TODO: Consider whether search belongs in this controller
    Ok(Json.toJson(searchService.fullSearchIndex))
