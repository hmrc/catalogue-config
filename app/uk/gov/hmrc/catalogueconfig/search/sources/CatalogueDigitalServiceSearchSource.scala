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
class CatalogueDigitalServiceSearchSource @Inject()(
    connector: CatalogueConnector
)(implicit ec: ExecutionContext) extends SearchSource {

  private val digitalServicesPath: String =
    "/digital-services/"

  private val deploymentsByDigitalServicePath: String =
    "/whats-running-where?digitalService="

  override def terms(): Future[Seq[SearchTerm]] =
    connector.allDigitalServices().map { digitalServices =>
      digitalServices.flatMap { ds =>
        val encodedDigitalService = SearchUrlEncoding.encodeQuery(ds)
        Seq(
          SearchTerm(
            linkType = "digital service",
            name     = ds,
            href     = s"$digitalServicesPath${SearchUrlEncoding.encodePathSegment(ds)}",
            weight   = 0.5f
          ),
          SearchTerm(
            linkType = "deployments (digital service)",
            name     = ds,
            href     = s"$deploymentsByDigitalServicePath$encodedDigitalService",
            weight   = 0.5f
          )
        )
      }
    }
}
