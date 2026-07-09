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

import uk.gov.hmrc.catalogueconfig.model.{BannerMenu, DropdownSeparator, MenuDropdown, Page, TopMenu}

import javax.inject.Singleton

object NavMenuService {

  val users: TopMenu        = TopMenu("Users", "users", defaultHref("users"))
  val teams: TopMenu        = TopMenu("Teams", "teams", defaultHref("teams"))
  val repositories: TopMenu = TopMenu("Repositories", "repositories", defaultHref("repositories"))

  // These top-menu items are dropdown entry points, not standalone pages.
  val deployments: TopMenu  = TopMenu("Deployments", "deployments")
  val shuttering: TopMenu   = TopMenu("Shuttering", "shuttering")
  val health: TopMenu       = TopMenu("Health", "health")
  val explore: TopMenu      = TopMenu("Explore", "explore")
  val docs: TopMenu         = TopMenu("Docs", "docs")

  val topLevelMenus: List[TopMenu] = List(
    users,
    teams,
    repositories,
    deployments,
    shuttering,
    health,
    explore,
    docs
  )

  // pages
  val createUser: Page                 = Page("Create a User", "create-user", "/create-user")
  val createServiceUser: Page          = Page("Create a Service User", "create-service-user", "/create-service-user")
  val offboardUsers: Page              = Page("Offboard Users", "offboard-users", "/offboard-users")

  val deployService: Page              = Page("Deploy Service", "deploy-service", "/deploy-service")
  val deploymentEvents: Page           = Page("Deployment Events", "deployment-events", "/deployments/production")
  val deploymentTimeline: Page         = Page("Version Timeline", "deployment-timeline", "/deployment-timeline")
  val whatsRunningWhere: Page          = Page("What's Running Where", "whats-running-where", "/whats-running-where")
 
  val shutterOverviewFrontend: Page    = Page("Shutter Overview - Frontend", "shutter-overview-frontend", "/shuttering-overview/frontend")
  val shutterOverviewApi: Page         = Page("Shutter Overview - Api", "shutter-overview-api", "/shuttering-overview/api")
  val shutterOverviewRate: Page        = Page("Shutter Overview - Rate", "shutter-overview-rate", "/shuttering-overview/rate")
  val shutterEvents: Page              = Page("Shutter Events", "shutter-events", "/shutter-events")

  val platformInitiatives: Page        = Page("Platform Initiatives", "platform-initiatives", "/platform-initiatives")

  val bobbyRules: Page                 = Page("Bobby Rules", "bobby-rules", "/bobbyrules")
  val bobbyViolations: Page            = Page("Bobby Violations", "bobby-violations", "/bobby-violations")

  val leakDetectionRules: Page         = Page("Leak Detection - Rules", "leak-detection-rules", "/leak-detection")
  val leakDetectionRepositories: Page  = Page("Leak Detection - Repositories", "leak-detection-repositories", "/leak-detection/repositories?includeViolations=true"  )

  val vulnerabilities: Page            = Page("Vulnerabilities", "vulnerabilities", "/vulnerabilities?curationStatus=ACTION_REQUIRED")
  val vulnerabilitiesServices: Page    = Page("Vulnerabilities - Services", "vulnerabilities-services", "/vulnerabilities/services")
  val vulnerabilitiesTimeline: Page    = Page("Vulnerabilities - Timeline", "vulnerabilities-timeline", "/vulnerabilities/timeline?curationStatus=ACTION_REQUIRED")

  val prCommenterRecommendations: Page = Page("PR-Commenter Recommendations", "pr-commenter-recommendations", "/pr-commenter/recommendations")

  val healthMetricsTimeline: Page      = Page("Health Metrics - Timeline", "health-metrics-timeline", "/health-metrics/timeline")
  val operationalMetrics: Page         = Page("Operational Metrics", "operational-metrics", "/health-metrics")

  val dependencyExplorer: Page         = Page("Dependency Explorer", "dependency-explorer", "/dependencyexplorer")
  val jdkExplorer: Page                = Page("JDK Explorer", "jdk-explorer", "/jdkexplorer")
  val sbtExplorer: Page                = Page("SBT Explorer", "sbt-explorer", "/sbtexplorer")
  val searchByUrl: Page                = Page("Search by URL", "search-by-url", "/search#")
  val searchConfig: Page               = Page("Search Config", "search-config", "/config/search")
  val searchCommissioningState: Page   = Page("Search Commissioning State", "search-commissioning-state", "/commissioning-state/search")
  val serviceMetrics: Page             = Page("Service Metrics", "service-metrics", "/service-metrics")
  val testResults: Page                = Page("Test Results", "test-results", "/tests")
  val configWarnings: Page             = Page("Config Warnings", "config-warnings", "/config/warnings/search")
  val costExplorer: Page               = Page("Cost Explorer", "cost-explorer", "/cost-explorer")
  val serviceProvision: Page           = Page("Service Provision", "service-provision", "/service-provision")

  val MdtpHandbook = Page("MDTP Handbook", "mdtp-handbook", Some("https://docs.tax.service.gov.uk/mdtp-handbook/"), external = true)
  val BlogPosts    = Page("Blog Posts", "blog-posts", Some("https://confluence.tools.tax.service.gov.uk/dosearchsite.action?cql=(label=catalogue and type=blogpost) order by created desc"), external = true)

  private def defaultHref(id: String): String =
    Option(id)
      .map(_.trim)
      .filter(_.nonEmpty)
      .fold("/")(value => s"/$value")
}

@Singleton
class NavMenuService {
  
  import NavMenuService._

  def buildMenu(): BannerMenu = {
    BannerMenu(
      brand =
        TopMenu(
          name = "MDTP",
          id = "mdtp",
          href = Some("/")
          ),

      topLevelLinks = NavMenuService.topLevelMenus,

      dropdowns =
        Seq(
          MenuDropdown(
            id = NavMenuService.users.id,
            name = NavMenuService.users.name,
            href = Some("/users"),
            items = Seq(
              createUser,
              createServiceUser,
              offboardUsers
              )
            ),

          MenuDropdown(
            id = NavMenuService.deployments.id,
            name = NavMenuService.deployments.name,
            href = None,
            items = Seq(
              deployService,
              deploymentEvents,
              deploymentTimeline,
              whatsRunningWhere
              )
            ),

          MenuDropdown(
            id = NavMenuService.shuttering.id,
            name = NavMenuService.shuttering.name,
            href = None,
            items = Seq(
              shutterOverviewFrontend,
              shutterOverviewApi,
              shutterOverviewRate,
              shutterEvents
              )
            ),

          MenuDropdown(
            id = NavMenuService.health.id,
            name = NavMenuService.health.name,
            href = None,
            items = Seq(
              platformInitiatives,

              bobbyRules,
              bobbyViolations,

              leakDetectionRules,
              leakDetectionRepositories,

              DropdownSeparator,
              vulnerabilities,
              vulnerabilitiesServices,
              vulnerabilitiesTimeline,

              prCommenterRecommendations,

              healthMetricsTimeline,
              operationalMetrics, // href not confirmed from rendered HTML; retained existing target
              )
            ),

          MenuDropdown(
            id = NavMenuService.explore.id,
            name = NavMenuService.explore.name,
            href = None,
            items = Seq(
              dependencyExplorer,
              jdkExplorer,
              sbtExplorer,
              searchByUrl,
              searchConfig,
              searchCommissioningState,
              serviceMetrics,
              testResults,
              configWarnings,
              costExplorer,
              serviceProvision
              )
            ),

          MenuDropdown(
            id = NavMenuService.docs.id,
            name = NavMenuService.docs.name,
            href = None,
            items = Seq(
              MdtpHandbook,
              BlogPosts
              )
            )

          )
      )
  }

}
