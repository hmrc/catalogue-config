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

import uk.gov.hmrc.catalogueconfig.connectors.PrCommenterConnector
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CataloguePrCommenterSearchSource @Inject()(
    connector: PrCommenterConnector
)(implicit ec: ExecutionContext) extends SearchSource {

  private val recommendationsPath: String =
    "/pr-commenter/recommendations?name="

  override def terms(): Future[Seq[SearchTerm]] =
    connector.allReports().map { reports =>
      reports.map { report =>
        SearchTerm(
          linkType = "recommendations",
          name     = report.name,
          href     = s"$recommendationsPath${SearchUrlEncoding.encodeQuery(report.name)}",
          weight   = 0.5f
        )
      }
    }
}
