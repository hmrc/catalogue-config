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

package uk.gov.hmrc.catalogueconfig

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import search.sources.{CatalogueDigitalServiceSearchSource, CataloguePrCommenterSearchSource, CatalogueRepositorySearchSource, CatalogueServiceConfigSearchSource, CatalogueTeamSearchSource, CatalogueUserSearchSource, OperationalMetricsSearchSource, StaticPageSearchSource}
import search.{SearchScheduler, SearchSource}

import java.time.Clock

class Module extends AbstractModule:

  override def configure(): Unit =
    bind(classOf[SearchScheduler]).asEagerSingleton()
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone) // inject if current time needs to be controlled in unit tests

    val sources = Multibinder.newSetBinder(binder(), classOf[SearchSource])
    sources.addBinding().to(classOf[StaticPageSearchSource])
    sources.addBinding().to(classOf[OperationalMetricsSearchSource])
    sources.addBinding().to(classOf[CatalogueTeamSearchSource])
    sources.addBinding().to(classOf[CatalogueDigitalServiceSearchSource])
    sources.addBinding().to(classOf[CatalogueRepositorySearchSource])
    sources.addBinding().to(classOf[CatalogueServiceConfigSearchSource])
    sources.addBinding().to(classOf[CatalogueUserSearchSource])
    sources.addBinding().to(classOf[CataloguePrCommenterSearchSource])
