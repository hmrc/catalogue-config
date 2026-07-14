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

import uk.gov.hmrc.catalogueconfig.config.AppConfig
import uk.gov.hmrc.catalogueconfig.connectors.CatalogueConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

/** Produces search terms for every repository in Catalogue.
  *
  * For each repo we emit:
  *   - a repo/service/library/prototype/test link pointing at the Catalogue repository page
  *   - a "leak" link pointing at the leak-detection branch summary page
  *
  * For Service repos we additionally emit:
  *   - a "deploy" link
  *   - a "config" link
  *   - a "timeline" link
  *   - a "commissioning state" link
  */
@Singleton
class CatalogueRepositorySearchSource @Inject()(
    connector: CatalogueConnector,
    appConfig: AppConfig
)(implicit ec: ExecutionContext) extends SearchSource {

  private def encodeQuery(s: String): String =
    URLEncoder.encode(s, StandardCharsets.UTF_8.name())

  private def encodePath(s: String): String =
    encodeQuery(s).replace("+", "%20")

  private def repoTypeLinkType(repoType: String): String =
    repoType match {
      case "Other" => "Repository"
      case other   => other
    }

  override def terms(): Future[Seq[SearchTerm]] =
    connector.allRepositories().map { repos =>
      repos.flatMap { repo =>
        val base = Seq(
          SearchTerm(
            linkType = repoTypeLinkType(repo.repoType),
            name     = repo.name,
            href     = appConfig.catalogueUrl(s"/repositories?name=${encodeQuery(repo.name)}"),
            weight   = 0.5f,
            hints    = Set("repository")
          ),
          SearchTerm(
            linkType = "leak",
            name     = repo.name,
            href     = appConfig.catalogueUrl(s"/leak-detection/repositories/${encodePath(repo.name)}"),
            weight   = 0.5f
          )
        )

        val serviceLinks =
          if (repo.repoType == "Service")
            Seq(
              SearchTerm(
                linkType = "deploy",
                name     = repo.name,
                href     = appConfig.catalogueUrl(s"/deploy-service?serviceName=${encodeQuery(repo.name)}")
              ),
              SearchTerm(
                linkType = "config",
                name     = repo.name,
                href     = appConfig.catalogueUrl(s"/search-config?serviceName=${encodeQuery(repo.name)}")
              ),
              SearchTerm(
                linkType = "timeline",
                name     = repo.name,
                href     = appConfig.catalogueUrl(s"/deployment-timeline?serviceName=${encodeQuery(repo.name)}")
              ),
              SearchTerm(
                linkType = "commissioning state",
                name     = repo.name,
                href     = appConfig.catalogueUrl(s"/search-commissioning-state?serviceName=${encodeQuery(repo.name)}")
              )
            )
          else Seq.empty

        base ++ serviceLinks
      }
    }
}
