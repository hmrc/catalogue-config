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

package uk.gov.hmrc.catalogueconfig.search

import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

@Singleton
class SearchIndexer @Inject()(
  sources: java.util.Set[SearchSource],
  searchIndex: SearchIndex
)(implicit ec: ExecutionContext)
  extends Logging {

  import scala.jdk.CollectionConverters._

  def rebuild(): Future[Unit] = {
    val allSources = sources.asScala.toSeq
    Future
      .sequence(
        allSources.map { source =>
          source.terms().map { terms =>
            logger.info(s"${source.getClass.getSimpleName} produced ${terms.size} search terms")
            terms
          }
        }
        )
      .map(_.flatten)
      .map { terms =>
        logger.info(s"Replacing search index with ${terms.size} total search terms from ${allSources.size} source(s)")
        searchIndex.replaceAll(terms)
      }
      .andThen {
        case Failure(e) => logger.error("Failed to rebuild search index", e)
      }
  }
}
