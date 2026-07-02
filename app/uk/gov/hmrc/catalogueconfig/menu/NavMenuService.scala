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

package uk.gov.hmrc.catalogueconfig.menu

import uk.gov.hmrc.catalogueconfig.model.{BannerMenu, MenuDropdown, MenuLink}

import javax.inject.{Inject, Singleton}

object NavMenuService
@Singleton
class NavMenuService @Inject(
  configuration: play.api.Configuration
):
  
  private def buildRelativeUrl(path: String): String =
    s"/${path.stripPrefix("/")}"


  def buildMenu(): BannerMenu =
    BannerMenu(
      brand =
        MenuLink(
          id = "mdtp",
          text = "MDTP",
          href = buildRelativeUrl("/")
          ),

      topLevelLinks =
        Seq(
          MenuLink("users", "Users", buildRelativeUrl("/users")),
          MenuLink("teams", "Teams", buildRelativeUrl("/teams")),
          MenuLink("repositories", "Repositories", buildRelativeUrl("/repositories"))

          , MenuLink(
            id = "new-item",
            text = "NEW ITEM",
            href = buildRelativeUrl("/new-item")
            )
          ),

      dropdowns =
        Seq(
          MenuDropdown(
            id = "deployments",
            text = "Deployments",
            items = Seq(
              MenuLink("deploy-service", "Deploy Service", buildRelativeUrl("/deploy-service")),
              MenuLink("deployment-events", "Events", buildRelativeUrl("/deployment-events")),
              MenuLink("deployment-timeline", "Timeline", buildRelativeUrl("/deployment-timeline")),
              MenuLink("whats-running-where", "What's Running Where", buildRelativeUrl("/whats-running-where"))
              )
            ),

          MenuDropdown(
            id = "shuttering",
            text = "Shuttering",
            items = Seq(
              MenuLink("shutter-overview-frontend", "Shutter Overview - Frontend", buildRelativeUrl("/shutter-overview/frontend")),
              MenuLink("shutter-overview-api", "Shutter Overview - Api", buildRelativeUrl("/shutter-overview/api")),
              MenuLink("shutter-overview-rate", "Shutter Overview - Rate", buildRelativeUrl("/shutter-overview/rate")),
              MenuLink("shutter-events", "Shutter Events", buildRelativeUrl("/shutter-events"))
              )
            ),

          MenuDropdown(
            id = "health",
            text = "Health",
            items = Seq(
              MenuLink("platform-initiatives", "Platform Initiatives", buildRelativeUrl("/platform-initiatives")),

              MenuLink("bobby-rules", "Bobby Rules", buildRelativeUrl("/bobby/rules")),
              MenuLink("bobby-violations", "Bobby Violations", buildRelativeUrl("/bobby/violations")),

              MenuLink("leak-detection-rules", "Leak Detection - Rules", buildRelativeUrl("/leak-detection/rules")),
              MenuLink("leak-detection-repositories", "Leak Detection - Repositories", buildRelativeUrl("/leak-detection/repositories")),

              MenuLink("vulnerabilities", "Vulnerabilities", buildRelativeUrl("/vulnerabilities")),
              MenuLink("vulnerabilities-services", "Vulnerabilities - Services", buildRelativeUrl("/vulnerabilities/services")),
              MenuLink("vulnerabilities-timeline", "Vulnerabilities - Timeline", buildRelativeUrl("/vulnerabilities/timeline")),

              MenuLink("pr-commenter-recommendations", "PR-Commenter Recommendations", buildRelativeUrl("/pr-commenter/recommendations")),

              MenuLink("health-metrics-timeline", "Health Metrics - Timeline", buildRelativeUrl("/health-metrics/timeline")),

              MenuLink("operational-metrics", "Operational Metrics", buildRelativeUrl("/health-metrics")) // TODO: need a 'hack' to make this work locally where we need to run in differnt apps
              )
            ),

          MenuDropdown(
            id = "explore",
            text = "Explore",
            items = Seq(
              MenuLink("dependency-explorer", "Dependency Explorer", buildRelativeUrl("/dependency-explorer")),
              MenuLink("jdk-explorer", "JDK Explorer", buildRelativeUrl("/jdk-explorer")),
              MenuLink("sbt-explorer", "SBT Explorer", buildRelativeUrl("/sbt-explorer")),
              MenuLink("search-by-url", "Search by URL", buildRelativeUrl("/search-by-url")),
              MenuLink("search-config", "Search Config", buildRelativeUrl("/search-config")),
              MenuLink("search-commissioning-state", "Search Commissioning State", buildRelativeUrl("/search-commissioning-state")),
              MenuLink("service-metrics", "Service Metrics", buildRelativeUrl("/service-metrics")),
              MenuLink("test-results", "Test Results", buildRelativeUrl("/test-results")),
              MenuLink("config-warnings", "Config Warnings", buildRelativeUrl("/config-warnings")),
              MenuLink("cost-explorer", "Cost Explorer", buildRelativeUrl("/cost-explorer")),
              MenuLink("service-provision", "Service Provision", buildRelativeUrl("/service-provision"))
              )
            ),

          MenuDropdown(
            id = "docs",
            text = "Docs",
            items = Seq(
              MenuLink(
                id = "mdtp-handbook",
                text = "MDTP Handbook",
                href = "https://docs.tax.service.gov.uk/mdtp-handbook/",
                external = true
                ),
              MenuLink(
                id = "blog-posts",
                text = "Blog Posts",
                href = "https://confluence.tools.tax.service.gov.uk/display/MDTPK/Blog+Posts",
                external = true
                )
              )
            )

          )
      )