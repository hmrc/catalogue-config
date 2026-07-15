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

import uk.gov.hmrc.catalogueconfig.connectors.CatalogueConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.{SearchSource, SearchUrlConfig}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CatalogueTeamSearchSource @Inject()(
    connector: CatalogueConnector,
    urlConfig: SearchUrlConfig
)(implicit ec: ExecutionContext) extends SearchSource {

  private val catalogueFrontendBaseUrl: String =
    urlConfig.catalogueFrontendBaseUrl

  private val teamsSearchPath: String =
    "/teams?name="

  private val deploymentsByTeamSearchPath: String =
    "/whats-running-where?teamName="

  override def terms(): Future[Seq[SearchTerm]] =
    connector.allTeams().map { teams =>
      teams.flatMap { team =>
        val encodedTeamName = SearchUrlEncoding.encodeQuery(team.name)
        Seq(
          SearchTerm(
            linkType = "team",
            name     = team.name,
            href     = s"$catalogueFrontendBaseUrl$teamsSearchPath$encodedTeamName",
            weight   = 0.5f
          ),
          SearchTerm(
            linkType = "deployments by team",
            name     = team.name,
            href     = s"$catalogueFrontendBaseUrl$deploymentsByTeamSearchPath$encodedTeamName",
            weight   = 0.5f
          )
        )
      }
    }
}
