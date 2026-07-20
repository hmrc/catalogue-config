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

package uk.gov.hmrc.catalogueconfig.connectors

import play.api.Logging
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import uk.gov.hmrc.catalogueconfig.model.PrCommenterReport
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PrCommenterConnector @Inject()(
  ws: WSClient,
  servicesConfig: ServicesConfig
)(implicit ec: ExecutionContext) extends Logging {

  private val baseUrl = servicesConfig.baseUrl("pr-commenter")
  
  def allReports(): Future[Seq[PrCommenterReport]] = {
    val url = s"$baseUrl/pr-commenter/reports"
    ws.url(url)
      .get()
      .map { response =>
        if (response.status == 200)
          Json.parse(response.body).as[Seq[PrCommenterReport]]
        else {
          logger.warn(s"PrCommenterConnector.allReports: unexpected status ${response.status} from $url")
          Seq.empty
        }
      }
      .recover { case ex =>
        logger.error(s"PrCommenterConnector.allReports: failed to call $url", ex)
        Seq.empty
      }
  }
}
