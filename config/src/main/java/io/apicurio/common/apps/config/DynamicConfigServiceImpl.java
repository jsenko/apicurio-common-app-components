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

package io.apicurio.common.apps.config;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;

import io.apicurio.common.apps.mt.TenantContext;
import io.quarkus.scheduler.Scheduled;

/**
 * @author eric.wittmann@gmail.com
 */
@ApplicationScoped
@SuppressWarnings("rawtypes")
public class DynamicConfigServiceImpl implements DynamicConfigService {

    private static final Object CACHED_NULL_VALUE = new Object();

    @Inject
    Logger log;

    @Inject
    TenantContext tenantContext;

    @Inject
    DynamicConfigStorage configStorage;

    private Map<DynamicConfigPropertyDef, Object> globalPropertyCache = new HashMap<>();
    private Map<String, Map<DynamicConfigPropertyDef, Object>> tenantPropertyCaches = new HashMap<>();
    private Instant lastRefresh = null;

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigService#get(io.apicurio.common.apps.config.DynamicConfigPropertyDef)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String get(DynamicConfigPropertyDef property) {
        return get(property, String.class);
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigService#getOptional(io.apicurio.common.apps.config.DynamicConfigPropertyDef)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Optional<String> getOptional(DynamicConfigPropertyDef property) {
        return getOptional(property, String.class);
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigService#get(io.apicurio.common.apps.config.DynamicConfigPropertyDef, java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(DynamicConfigPropertyDef<T> property, Class<T> propertyType) {
        // Possibly check the tenant property cache for a value.  If found return it.
        T rval = (T) tenantPropertyCache().get(property);

        // If not found, check for a global property.
        if (rval == null) {
            rval = (T) globalPropertyCache.computeIfAbsent(property, (key) -> {
                Optional<T> value = ConfigProvider.getConfig().getOptionalValue(property.name(), propertyType);
                T newMappedValue = value.orElse(property.defaultValue());
                // Can't cache a null value, so convert to the cached null value.
                if (newMappedValue == null) {
                    newMappedValue = (T) CACHED_NULL_VALUE;
                }
                return newMappedValue;
            });
        }

        // Revert the cached null value to actual null.
        if (rval == CACHED_NULL_VALUE) {
            rval = null;
        }

        return rval;
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigService#getOptional(io.apicurio.common.apps.config.DynamicConfigPropertyDef, java.lang.Class)
     */
    @Override
    public <T> Optional<T> getOptional(DynamicConfigPropertyDef<T> property, Class<T> propertyType) {
        T rval = get(property, propertyType);
        return Optional.ofNullable(rval);
    }

    public <T> T set(DynamicConfigPropertyDef property, T newValue) {
        DynamicConfigPropertyDto propertyDto = DynamicConfigPropertyDto.create(property.name(), newValue);
        configStorage.setConfigProperty(propertyDto);
        return newValue;
    }

    @SuppressWarnings("unchecked")
    private Map<DynamicConfigPropertyDef, Object> tenantPropertyCache() {
        String getTenantId = tenantContext.getTenantId();
        return tenantPropertyCaches.computeIfAbsent(getTenantId, key -> {
            Map<String, Object> tenantProperties = loadTenantProperties();
            Map<DynamicConfigPropertyDef, Object> cache = new HashMap<>();
            tenantProperties.forEach((k,v) -> {
                DynamicConfigPropertyDef property = new DynamicConfigPropertyDef(k, v);
                if (property != null) {
                    cache.put(property, v);
                }
            });
            return cache;
        });
    }

    protected Map<String, Object> loadTenantProperties() {
        List<DynamicConfigPropertyDto> configProperties = configStorage.getConfigProperties();
        Map<String, Object> rval = new HashMap<>();
        configProperties.forEach(dto -> {
            rval.put(dto.getName(), convertValue(dto));
        });
        return rval;
    }

    /**
     * Scheduled job to reload configuration properties that might have been changed.
     */
    @Scheduled(concurrentExecution = io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP, every = "{app.config.refresh.every}")
    void run() {
        try {
            log.debug("Running config property refresh job at {}", Instant.now());
            refresh();
        } catch (Exception ex) {
            log.error("Exception thrown when running config property refresh job.", ex);
        }
    }

    private void refresh() {
        Instant now = Instant.now();
        if (lastRefresh != null) {
            List<String> getTenantIds = configStorage.getTenantsWithStaleConfigProperties(lastRefresh);
            getTenantIds.forEach(getTenantId -> invalidateTenantCache(getTenantId));
        }
        lastRefresh = now;
    }

    /**
     * @param getTenantId
     */
    private void invalidateTenantCache(String getTenantId) {
        tenantPropertyCaches.remove(getTenantId);
    }

    private static Object convertValue(DynamicConfigPropertyDto property) {
        String name = property.getName();
        String type = property.getType();
        String value = property.getValue();

        if (value == null) {
            return null;
        }

        if ("java.lang.String".equals(type)) {
            return value;
        }
        if ("java.lang.Boolean".equals(type)) {
            return "true".equals(value);
        }
        if ("java.lang.Integer".equals(type)) {
            return Integer.valueOf(value);
        }
        if ("java.lang.Long".equals(type)) {
            return Long.valueOf(value);
        }
        throw new UnsupportedOperationException("Configuration property type not supported: " + type + " for property with name: " + name);
    }
}
