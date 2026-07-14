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
import uk.gov.hmrc.catalogueconfig.connectors.ServiceConfigsConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

/** Emits additional "Service" search terms sourced from service-configs service-repo-name mappings.
  * This mirrors Catalogue's use of `serviceConfigsConnector.serviceRepoMappings` to catch services
  * whose names differ from their repo names.
  */
@Singleton
class CatalogueServiceConfigSearchSource @Inject()(
    connector: ServiceConfigsConnector,
    appConfig: AppConfig
)(implicit ec: ExecutionContext) extends SearchSource {

  private def encodeQuery(s: String): String =
    URLEncoder.encode(s, StandardCharsets.UTF_8.name())

  private def encodePath(s: String): String =
    encodeQuery(s).replace("+", "%20")

  override def terms(): Future[Seq[SearchTerm]] =
    connector.serviceRepoMappings().map { mappings =>
      mappings.map { mapping =>
        SearchTerm(
          linkType = "Service",
          name     = mapping.serviceName,
          href     = appConfig.catalogueUrl(s"/service/${encodePath(mapping.serviceName)}"),
          weight   = 0.5f,
          hints    = Set("repository")
        )
      }
    }
}
