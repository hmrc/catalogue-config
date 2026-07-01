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

import uk.gov.hmrc.catalogueconfig.model.SearchTerm

import javax.inject.Singleton

@Singleton
class SearchService {

  def searchIndex: List[SearchTerm] = {
    // example search index
    List(
      SearchTerm(
        linkType = "service",
        name = "Users",
        href = "/users",
        weight = 1.0f,
        hints = Set("user management", "account management"),
        openInNewWindow = false
      ),
      SearchTerm(
        linkType = "service",
        name = "Teams",
        href = "/teams",
        weight = 1.0f,
        hints = Set("team management", "group management"),
        openInNewWindow = false
      ),
      SearchTerm(
        linkType = "user",
        name = "John Doe",
        href = "/users/john-doe",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "user",
        name = "Bob",
        href = "/users/bob",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "leak",
        name = "placeholder-leak",
        href = "/leak-detection/repositories/some-repo-with-leak",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "deploy",
        name = "repo-deploy-placeholder",
        href = "/deploy-service?serviceName=repo-for-deploy",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "config",
        name = "placeholder-config",
        href = "/service/some-placeholder-repo/config",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "timeline",
        name = "placeholder-for-timeline",
        href = "/deployment-timeline?repo-for-timeline",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "commissioning state",
        name = "placeholder-commissioning-state",
        href = "/service/placeholder-repo/commissioning-state",
        weight = 1.0f
      ),
      SearchTerm(
        linkType = "recommendations",
        name = "placeholder-repo-for-recommendations",
        href = "/pr-commenter/recommendations?name=placeholder-repo",
        weight = 1.0f
      )
      )

  }
}
