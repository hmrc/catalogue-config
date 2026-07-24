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

import play.api.Configuration
import uk.gov.hmrc.catalogueconfig.connectors.OperationalMetricsConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OperationalMetricsSearchSource @Inject()(
    connector: OperationalMetricsConnector,
    config: Configuration
)(implicit ec: ExecutionContext) extends SearchSource {

  private val operationalMetricsBaseUrl: String =
    config.getOptional[String]("frontend-base-urls.operational-metrics-frontend").getOrElse("").stripSuffix("/")

  private val serviceQueryPath: String =
    "?service="

  override def terms(): Future[Seq[SearchTerm]] =
    connector.getServiceLeadTimes().map { services =>
      services.map { service =>
        val encodedName = SearchUrlEncoding.encodeQuery(service.serviceName)
        val hints       = service.leadTimes.flatMap(lt => Seq(lt.environment, lt.version)).toSet
        SearchTerm(
          linkType = "operational metric",
          name = service.serviceName,
          href = s"$operationalMetricsBaseUrl$serviceQueryPath$encodedName",
          weight = 0.7f,
          hints = hints,
          openInNewWindow = false
          )
      }
    }
}
