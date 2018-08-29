/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.metrics.web.reactive;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.metrics.web.reactive.server.DefaultWebFluxTagsProvider;
import org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WebFluxMetricsAutoConfiguration}
 *
 * @author Brian Clozel
 */
public class WebFluxMetricsAutoConfigurationTests {

	private ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MetricsAutoConfiguration.class,
					SimpleMetricsExportAutoConfiguration.class,
					WebFluxMetricsAutoConfiguration.class));

	@Test
	public void shouldProvideWebFluxMetricsBeans() {
		this.contextRunner.run((context) -> {
			assertThat(context).getBeans(MetricsWebFilter.class).hasSize(1);
			assertThat(context).getBeans(DefaultWebFluxTagsProvider.class).hasSize(1);
		});
	}

	@Test
	public void shouldNotOverrideCustomTagsProvider() {
		this.contextRunner.withUserConfiguration(CustomWebFluxTagsProviderConfig.class)
				.run((context) -> assertThat(context).getBeans(WebFluxTagsProvider.class)
						.hasSize(1).containsKey("customWebFluxTagsProvider"));
	}

	@Configuration
	protected static class CustomWebFluxTagsProviderConfig {

		@Bean
		public WebFluxTagsProvider customWebFluxTagsProvider() {
			return mock(WebFluxTagsProvider.class);
		}

	}

}
