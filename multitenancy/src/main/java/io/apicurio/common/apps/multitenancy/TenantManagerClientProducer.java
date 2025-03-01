/*
 * Copyright 2022 Red Hat
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

package io.apicurio.common.apps.multitenancy;

import io.apicurio.common.apps.util.OptionalBean;
import io.apicurio.rest.client.VertxHttpClientProvider;
import io.apicurio.rest.client.auth.Auth;
import io.apicurio.rest.client.auth.OidcAuth;
import io.apicurio.rest.client.auth.exception.AuthErrorHandler;
import io.apicurio.rest.client.config.ApicurioClientConfig;
import io.apicurio.rest.client.spi.ApicurioHttpClient;
import io.apicurio.tenantmanager.client.TenantManagerClient;
import io.apicurio.tenantmanager.client.TenantManagerClientImpl;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.DeploymentException;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carles Arnal
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
public class TenantManagerClientProducer {

    @Inject
    Logger log;

    @Inject
    MultitenancyProperties properties;

    @Inject
    Vertx vertx;

    @Produces
    @ApplicationScoped
    OptionalBean<TenantManagerClient> produce() {

        if (properties.isMultitenancyEnabled() & !properties.isStandaloneMultitenancyEnabled()) {

            if (properties.getTenantManagerUrl().isEmpty()) {
                throw new DeploymentException("Unsupported configuration, \"app.enable.multitenancy\" is enabled " +
                        "but the no \"app.tenant.manager.url\" is provided");
            }

            Map<String, Object> clientConfigs = new HashMap<>();
            if (properties.getTenantManagerCAFilePath().isPresent() && !properties.getTenantManagerCAFilePath().get().isBlank()) {
                clientConfigs.put(ApicurioClientConfig.APICURIO_REQUEST_CA_BUNDLE_LOCATION, properties.getTenantManagerCAFilePath().get());
            }

            Auth auth = null;
            if (properties.isTenantManagerAuthEnabled()) {

                if (properties.getTenantManagerAuthUrl().isEmpty() ||
                        properties.getTenantManagerClientId().isEmpty() ||
                        properties.getTenantManagerClientSecret().isEmpty()) {
                    throw new DeploymentException("Unsupported configuration, \"app.enable.multitenancy\" is enabled " +
                            "\"app.enable.auth\" is enabled " +
                            "but the no auth properties aren't properly configured");
                }

                ApicurioHttpClient httpClient = new VertxHttpClientProvider(vertx).create(properties.getTenantManagerAuthUrl().get(), Collections.emptyMap(), null, new AuthErrorHandler());

                Duration tokenExpirationReduction = null;
                if (properties.getTenantManagerAuthTokenExpirationReductionMs().isPresent()) {
                    log.info("Using configured tenant-manager auth token expiration reduction {}", properties.getTenantManagerAuthTokenExpirationReductionMs().get());
                    tokenExpirationReduction = Duration.ofMillis(properties.getTenantManagerAuthTokenExpirationReductionMs().get());
                }

                auth = new OidcAuth(httpClient,
                        properties.getTenantManagerClientId().get(),
                        properties.getTenantManagerClientSecret().get(),
                        tokenExpirationReduction);
            }

            return OptionalBean.of(new TenantManagerClientImpl(properties.getTenantManagerUrl().get(), clientConfigs, auth));

        }

        return OptionalBean.empty();
    }
}
