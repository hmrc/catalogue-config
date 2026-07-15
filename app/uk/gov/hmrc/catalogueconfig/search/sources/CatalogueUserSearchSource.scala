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

package uk.gov.hmrc.catalogueconfig.search.sources

import uk.gov.hmrc.catalogueconfig.connectors.UserManagementConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CatalogueUserSearchSource @Inject()(
    connector: UserManagementConnector,
    servicesConfig: ServicesConfig
)(implicit ec: ExecutionContext) extends SearchSource {

  private val catalogueFrontendBaseUrl: String =
    servicesConfig.baseUrl("catalogue-frontend").stripSuffix("/")

  private val usersPath: String =
    "/users/"

  override def terms(): Future[Seq[SearchTerm]] =
    connector.getActiveUsers().map { users =>
      users.map { user =>
        SearchTerm(
          linkType = "users",
          name     = user.username,
          href     = s"$catalogueFrontendBaseUrl$usersPath${SearchUrlEncoding.encodePathSegment(user.username)}",
          weight   = 0.5f
        )
      }
    }
}
