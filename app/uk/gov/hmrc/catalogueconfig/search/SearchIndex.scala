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

import uk.gov.hmrc.catalogueconfig.model.SearchTerm

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton

@Singleton
class SearchIndex {

  private[search] val cachedIndex =
    new AtomicReference[Map[String, Seq[SearchTerm]]](Map.empty)

  private val cachedTerms =
    new AtomicReference[Seq[SearchTerm]](Seq.empty)

  def replaceAll(terms: Seq[SearchTerm]): Unit = {
    cachedTerms.set(terms)
    cachedIndex.set(SearchIndex.optimizeIndex(terms))
  }

  def search(query: Seq[String]): Seq[SearchTerm] =
    SearchIndex.search(query, cachedIndex.get())

  def allTerms: Seq[SearchTerm] =
    cachedTerms.get()
}

object SearchIndex {

  def normalizeTerm(term: String): String =
    term.toLowerCase.replaceAll("[ \\-_]", "")

  private[search] def search(
    query: Seq[String],
    index: Map[String, Seq[SearchTerm]]
  ): Seq[SearchTerm] = {
    val normalised = query.map(normalizeTerm)
    normalised
      .foldLeft(index.getOrElse(normalised.head.slice(0, 3), Seq.empty)) { (acc, cur) =>
        acc.filter((item: SearchTerm) => item.terms.exists(_.contains(cur)))
      }
      .map { st =>
        if (normalised.exists(_.equalsIgnoreCase(st.name))) st.copy(weight = 1f)
        else st
      }
      .sortBy(st => (-st.weight, st.name.toLowerCase))
      .distinct
  }

  def optimizeIndex(index: Seq[SearchTerm]): Map[String, Seq[SearchTerm]] =
    index
      .flatMap { st =>
        (st.linkType.sliding(3, 1) ++ st.name.sliding(3, 1) ++ st.hints.mkString.sliding(3, 1))
          .map(_.toLowerCase -> st)
      }
      .groupBy(_._1)
      .view
      .mapValues(_.map(_._2))
      .toMap
}
