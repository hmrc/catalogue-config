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

import uk.gov.hmrc.catalogueconfig.model.{BannerMenu, MenuDropdown, MenuLink, NavTarget, Page, TopMenu}

import javax.inject.Singleton

object NavMenuService {

  def defaultHref(id: String): String =
    Option(id)
      .map(_.trim)
      .filter(_.nonEmpty)
      .fold("/")(value => s"/$value")

  val users: TopMenu        = TopMenu("Users", "users", "View and manage users", defaultHref("users"))
  val teams: TopMenu        = TopMenu("Teams", "teams", "View and manage teams", defaultHref("teams"))
  val repositories: TopMenu = TopMenu("Repositories", "repositories", "View and manage repositories", defaultHref("repositories"))
  // These top-menu items are dropdown entry points, not standalone pages.
  val deployments: TopMenu  = TopMenu("Deployments", "deployments", "View and manage deployments")
  val shuttering: TopMenu   = TopMenu("Shuttering", "shuttering", "View and manage shuttering")
  val health: TopMenu       = TopMenu("Health", "health", "View and manage health")
  val explore: TopMenu      = TopMenu("Explore", "explore", "Explore services")
  val docs: TopMenu         = TopMenu("Docs", "docs", "View documentation")

  val availableTopMenus: List[TopMenu] = List(
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
  val createUser: Page                 = Page("Create a User", "create-user", "Create a user", "/create-user")
  val createServiceUser: Page          = Page("Create a Service User", "create-service-user", "Create a service user", "/create-service-user")
  val offboardUsers: Page              = Page("Offboard Users", "offboard-users", "Offboard users", "/offboard-users")

  val deployService: Page              = Page("Deploy Service", "deploy-service", "Deploy a service", "/deploy-service")
  val deploymentEvents: Page           = Page("Deployment Events", "deployment-events", "View deployment events", "/deployments/production")
  val deploymentTimeline: Page         = Page("Version Timeline", "deployment-timeline", "View app versions across environments over time", "/deployment-timeline")
  val whatsRunningWhere: Page          = Page("What's Running Where", "whats-running-where", "View what is running where", "/whats-running-where")
 
  val shutterOverviewFrontend: Page    = Page("Shutter Overview - Frontend", "shutter-overview-frontend", "View shutter overview for frontend", "/shuttering-overview/frontend")
  val shutterOverviewApi: Page         = Page("Shutter Overview - Api", "shutter-overview-api", "View shutter overview for API", "/shuttering-overview/api")
  val shutterOverviewRate: Page        = Page("Shutter Overview - Rate", "shutter-overview-rate", "View shutter overview for rate", "/shuttering-overview/rate")
  val shutterEvents: Page              = Page("Shutter Events", "shutter-events", "View shutter events", "/shutter-events")

  val platformInitiatives: Page        = Page("Platform Initiatives", "platform-initiatives", "View platform initiatives", "/platform-initiatives")

  val bobbyRules: Page                 = Page("Bobby Rules", "bobby-rules", "View Bobby rules", "/bobbyrules")
  val bobbyViolations: Page            = Page("Bobby Violations", "bobby-violations", "View Bobby violations", "/bobby-violations")

  val leakDetectionRules: Page         = Page("Leak Detection - Rules", "leak-detection-rules", "View leak detection rules", "/leak-detection") 
  val leakDetectionRepositories: Page  = Page("Leak Detection - Repositories", "leak-detection-repositories", "View leak detection repositories", "/leak-detection/repositories?includeViolations=true"  )

  val vulnerabilities: Page            = Page("Vulnerabilities", "vulnerabilities", "View vulnerabilities", "/vulnerabilities?curationStatus=ACTION_REQUIRED")
  val vulnerabilitiesServices: Page    = Page("Vulnerabilities - Services", "vulnerabilities-services", "View vulnerabilities by service", "/vulnerabilities/services")
  val vulnerabilitiesTimeline: Page    = Page("Vulnerabilities - Timeline", "vulnerabilities-timeline", "View vulnerabilities timeline", "/vulnerabilities/timeline?curationStatus=ACTION_REQUIRED")

  val prCommenterRecommendations: Page = Page("PR-Commenter Recommendations", "pr-commenter-recommendations", "View PR-Commenter recommendations", "/pr-commenter/recommendations")

  val healthMetricsTimeline: Page      = Page("Health Metrics - Timeline", "health-metrics-timeline", "View health metrics timeline", "/health-metrics/timeline")
  val operationalMetrics: Page         = Page("Operational Metrics", "operational-metrics", "View health and operational metrics", "/health-metrics")

  val dependencyExplorer: Page         = Page("Dependency Explorer", "dependency-explorer", "Explore service dependencies", "/dependencyexplorer")
  val jdkExplorer: Page                = Page("JDK Explorer", "jdk-explorer", "Explore JDK usage", "/jdkexplorer")
  val sbtExplorer: Page                = Page("SBT Explorer", "sbt-explorer", "Explore SBT usage", "/sbtexplorer")
  val searchByUrl: Page                = Page("Search by URL", "search-by-url", "Search services by URL", "/search#")
  val searchConfig: Page               = Page("Search Config", "search-config", "Search service configuration", "/config/search")
  val searchCommissioningState: Page   = Page("Search Commissioning State", "search-commissioning-state", "Search commissioning state", "/commissioning-state/search")
  val serviceMetrics: Page             = Page("Service Metrics", "service-metrics", "View service metrics", "/service-metrics")
  val testResults: Page                = Page("Test Results", "test-results", "View test results", "/tests")
  val configWarnings: Page             = Page("Config Warnings", "config-warnings", "View configuration warnings", "/config/warnings/search")
  val costExplorer: Page               = Page("Cost Explorer", "cost-explorer", "Explore service costs", "/cost-explorer")
  val serviceProvision: Page           = Page("Service Provision", "service-provision", "View service provisioning status and details", "/service-provision")

}

@Singleton
class NavMenuService {
  
  import NavMenuService._

  private def buildRelativeUrl(target: NavTarget): String = { // TODO: add dev mode ?
    target.href.map(_.trim)
               .map(href =>
                 if (href.isBlank) "/"
                 else if (href.startsWith("/") || href.startsWith("#")) href
                 else s"/${href.stripPrefix("/")}"
               )
               .getOrElse("#")
  }

  def buildMenu(): BannerMenu = {
    BannerMenu(
      brand =
        TopMenu(
          name = "MDTP",
          id = "mdtp",
          description = "MDTP",
          href = Some("/")
        ),

      topLevelLinks = NavMenuService.availableTopMenus,

      dropdowns =
        Seq(
          MenuDropdown(
            id = NavMenuService.users.id,
            text = NavMenuService.users.name,
            href = Some(buildRelativeUrl(users)),
            items = Seq(
              MenuLink(NavMenuService.createUser.id, NavMenuService.createUser.name, buildRelativeUrl(NavMenuService.createUser)),
              MenuLink(NavMenuService.createServiceUser.id, NavMenuService.createServiceUser.name, buildRelativeUrl(NavMenuService.createServiceUser)),
              MenuLink(NavMenuService.offboardUsers.id, NavMenuService.offboardUsers.name, buildRelativeUrl(NavMenuService.offboardUsers))
              )
            ),

          MenuDropdown(
            id = NavMenuService.deployments.id,
            text = NavMenuService.deployments.name,
            href = None,
            items = Seq(
              MenuLink(NavMenuService.deployService.id, NavMenuService.deployService.name, buildRelativeUrl(NavMenuService.deployService)),
              MenuLink(NavMenuService.deploymentEvents.id, NavMenuService.deploymentEvents.name, buildRelativeUrl(NavMenuService.deploymentEvents)),
              MenuLink(NavMenuService.deploymentTimeline.id, NavMenuService.deploymentTimeline.name, buildRelativeUrl(NavMenuService.deploymentTimeline)),
              MenuLink(NavMenuService.whatsRunningWhere.id, NavMenuService.whatsRunningWhere.name, buildRelativeUrl(NavMenuService.whatsRunningWhere))
              )
            ),

          MenuDropdown(
            id = NavMenuService.shuttering.id,
            text = NavMenuService.shuttering.name,
            href = None,
            items = Seq(
              MenuLink(NavMenuService.shutterOverviewFrontend.id, NavMenuService.shutterOverviewFrontend.name, buildRelativeUrl(NavMenuService.shutterOverviewFrontend)),
              MenuLink(NavMenuService.shutterOverviewApi.id, NavMenuService.shutterOverviewApi.name, buildRelativeUrl(NavMenuService.shutterOverviewApi)),
              MenuLink(NavMenuService.shutterOverviewRate.id, NavMenuService.shutterOverviewRate.name, buildRelativeUrl(NavMenuService.shutterOverviewRate)),
              MenuLink(NavMenuService.shutterEvents.id, NavMenuService.shutterEvents.name, buildRelativeUrl(NavMenuService.shutterEvents))
              )
            ),

          MenuDropdown(
            id = NavMenuService.health.id,
            text = NavMenuService.health.name,
            href = None,
            items = Seq(
              MenuLink(NavMenuService.platformInitiatives.id, NavMenuService.platformInitiatives.name, buildRelativeUrl(NavMenuService.platformInitiatives)),

              MenuLink(NavMenuService.bobbyRules.id, NavMenuService.bobbyRules.name, buildRelativeUrl(NavMenuService.bobbyRules)),
              MenuLink(NavMenuService.bobbyViolations.id, NavMenuService.bobbyViolations.name, buildRelativeUrl(NavMenuService.bobbyViolations)),

              MenuLink(NavMenuService.leakDetectionRules.id, NavMenuService.leakDetectionRules.name, buildRelativeUrl(NavMenuService.leakDetectionRules)),
              MenuLink(NavMenuService.leakDetectionRepositories.id, NavMenuService.leakDetectionRepositories.name, buildRelativeUrl(NavMenuService.leakDetectionRepositories)),

              MenuLink(NavMenuService.vulnerabilities.id, NavMenuService.vulnerabilities.name, buildRelativeUrl(NavMenuService.vulnerabilities)),
              MenuLink(NavMenuService.vulnerabilitiesServices.id, NavMenuService.vulnerabilitiesServices.name, buildRelativeUrl(NavMenuService.vulnerabilitiesServices)),
              MenuLink(NavMenuService.vulnerabilitiesTimeline.id, NavMenuService.vulnerabilitiesTimeline.name, buildRelativeUrl(NavMenuService.vulnerabilitiesTimeline)),

              MenuLink(NavMenuService.prCommenterRecommendations.id, NavMenuService.prCommenterRecommendations.name, buildRelativeUrl(NavMenuService.prCommenterRecommendations)),

              MenuLink(NavMenuService.healthMetricsTimeline.id, NavMenuService.healthMetricsTimeline.name, buildRelativeUrl(NavMenuService.healthMetricsTimeline)),
              MenuLink(NavMenuService.operationalMetrics.id, NavMenuService.operationalMetrics.name, buildRelativeUrl(NavMenuService.operationalMetrics)), // href not confirmed from rendered HTML; retained existing target
              )
            ),

          MenuDropdown(
            id = NavMenuService.explore.id,
            text = NavMenuService.explore.name,
            href = None,
            items = Seq(
              MenuLink(NavMenuService.dependencyExplorer.id, NavMenuService.dependencyExplorer.name, buildRelativeUrl(NavMenuService.dependencyExplorer)),
              MenuLink(NavMenuService.jdkExplorer.id, NavMenuService.jdkExplorer.name, buildRelativeUrl(NavMenuService.jdkExplorer)),
              MenuLink(NavMenuService.sbtExplorer.id, NavMenuService.sbtExplorer.name, buildRelativeUrl(NavMenuService.sbtExplorer)),
              MenuLink(NavMenuService.searchByUrl.id, NavMenuService.searchByUrl.name, buildRelativeUrl(NavMenuService.searchByUrl)),
              MenuLink(NavMenuService.searchConfig.id, NavMenuService.searchConfig.name, buildRelativeUrl(NavMenuService.searchConfig)),
              MenuLink(NavMenuService.searchCommissioningState.id, NavMenuService.searchCommissioningState.name, buildRelativeUrl(NavMenuService.searchCommissioningState)),
              MenuLink(NavMenuService.serviceMetrics.id, NavMenuService.serviceMetrics.name, buildRelativeUrl(NavMenuService.serviceMetrics)),
              MenuLink(NavMenuService.testResults.id, NavMenuService.testResults.name, buildRelativeUrl(NavMenuService.testResults)),
              MenuLink(NavMenuService.configWarnings.id, NavMenuService.configWarnings.name, buildRelativeUrl(NavMenuService.configWarnings)),
              MenuLink(NavMenuService.costExplorer.id, NavMenuService.costExplorer.name, buildRelativeUrl(NavMenuService.costExplorer)),
              MenuLink(NavMenuService.serviceProvision.id, NavMenuService.serviceProvision.name, buildRelativeUrl(NavMenuService.serviceProvision))
              )
            ),

          MenuDropdown(
            id = NavMenuService.docs.id,
            text = NavMenuService.docs.name,
            href = None,
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
                href = "https://confluence.tools.tax.service.gov.uk/dosearchsite.action?cql=(label=catalogue and type=blogpost) order by created desc",
                external = true
                )
              )
            )

          )
      )
  }

}
