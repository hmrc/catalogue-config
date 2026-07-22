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
import uk.gov.hmrc.catalogueconfig.model.SearchTerm
import uk.gov.hmrc.catalogueconfig.search.SearchSource

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class StaticPageSearchSource @Inject()(config: Configuration) extends SearchSource {

  private val catalogueBaseUrl: String =
    config.getOptional[String]("frontend-base-urls.catalogue-frontend").getOrElse("").stripSuffix("/")

  private val operationalMetricsBaseUrl: String =
    config.getOptional[String]("frontend-base-urls.operational-metrics-frontend").getOrElse("").stripSuffix("/")

  private val handbookUrl: String =
    config.getOptional[String]("docs.handbookUrl")
      .getOrElse("https://docs.tax.service.gov.uk/mdtp-handbook/")

  private val blogPostsUrl: String =
    config.getOptional[String]("docs.blogPostsUrl")
      .getOrElse("https://confluence.tools.tax.service.gov.uk/display/MDTPK/Blog+Posts")

  private def catalogueUrl(path: String): String =
    s"$catalogueBaseUrl/${path.stripPrefix("/")}"

  private val staticTerms: Seq[SearchTerm] = Seq(
    SearchTerm("page", "teams",                           catalogueUrl("/teams"),                           1.0f),
    SearchTerm("page", "repositories",                    catalogueUrl("/repositories"),                    1.0f),
    SearchTerm("page", "users",                           catalogueUrl("/users"),                           1.0f),
    SearchTerm("page", "dependency explorer",             catalogueUrl("/dependency-explorer"),             1.0f, Set("depex")),
    SearchTerm("page", "jdk explorer",                    catalogueUrl("/jdk-explorer"),                    1.0f, Set("jdk", "jre")),
    SearchTerm("page", "sbt explorer",                    catalogueUrl("/sbt-explorer"),                    1.0f),
    SearchTerm("page", "leaks",                           catalogueUrl("/leak-detection/rules"),            1.0f),
    SearchTerm("page", "bobby rules",                     catalogueUrl("/bobby/rules"),                     1.0f),
    SearchTerm("page", "bobby violations",                catalogueUrl("/bobby/violations"),                1.0f),
    SearchTerm("page", "whats running where",             catalogueUrl("/whats-running-where"),             1.0f, Set("wrw")),
    SearchTerm("page", "deployment events",               catalogueUrl("/deployment-events"),               1.0f),
    SearchTerm("page", "deployment timeline",             catalogueUrl("/deployment-timeline"),             1.0f),
    SearchTerm("page", "deploy service",                  catalogueUrl("/deploy-service"),                  1.0f),
    SearchTerm("page", "shutter overview frontend",       catalogueUrl("/shutter-overview/frontend"),       1.0f),
    SearchTerm("page", "shutter overview api",            catalogueUrl("/shutter-overview/api"),            1.0f),
    SearchTerm("page", "shutter overview rate",           catalogueUrl("/shutter-overview/rate"),           1.0f),
    SearchTerm("page", "shutter events",                  catalogueUrl("/shutter-events"),                  1.0f),
    SearchTerm("page", "pr commenter recommendations",    catalogueUrl("/pr-commenter/recommendations"),    1.0f),
    SearchTerm("page", "search config",                   catalogueUrl("/search-config"),                   1.0f),
    SearchTerm("page", "config warnings",                 catalogueUrl("/config-warnings"),                 1.0f),
    SearchTerm("page", "cost explorer",                   catalogueUrl("/cost-explorer"),                   1.0f),
    SearchTerm("page", "service provision",               catalogueUrl("/service-provision"),               1.0f),
    SearchTerm("page", "create repository",               catalogueUrl("/create-repository"),               1.0f),
    SearchTerm("page", "search commissioning state",      catalogueUrl("/search-commissioning-state"),      1.0f),
    SearchTerm("page", "service metrics",                 catalogueUrl("/service-metrics"),                 1.0f),
    SearchTerm("page", "health metrics timeline",         catalogueUrl("/health-metrics/timeline"),         1.0f),
    SearchTerm("page", "vulnerabilities",                 catalogueUrl("/vulnerabilities"),                 1.0f),
    SearchTerm("page", "vulnerabilities services",        catalogueUrl("/vulnerabilities/services"),        1.0f),
    SearchTerm("page", "vulnerabilities timeline",        catalogueUrl("/vulnerabilities/timeline"),        1.0f),
    SearchTerm("page", "test results",                    catalogueUrl("/test-results"),                    1.0f),
    SearchTerm("page", "search by url",                   catalogueUrl("/search-by-url"),                   1.0f),
    SearchTerm("page", "platform initiatives",            catalogueUrl("/platform-initiatives"),            1.0f),
    SearchTerm("page", "operational metrics",             operationalMetricsBaseUrl,                        1.0f),
    SearchTerm("docs", "mdtp handbook",                   handbookUrl,                                      1.0f, openInNewWindow = true),
    SearchTerm("docs", "blog posts",                      blogPostsUrl,                                     1.0f, openInNewWindow = true)
  )

  override def terms(): Future[Seq[SearchTerm]] =
    Future.successful(staticTerms)
}
