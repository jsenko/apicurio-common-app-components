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

package io.apicurio.common.apps.config.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;

import io.apicurio.common.apps.config.DynamicConfigPropertyDef;
import io.apicurio.common.apps.config.DynamicConfigPropertyIndex;
import io.apicurio.common.apps.config.DynamicConfigPropertyList;

/**
 * @author eric.wittmann@gmail.com
 */
@ApplicationScoped
public class DynamicConfigPropertyIndexImpl implements DynamicConfigPropertyIndex {

    private Map<String, DynamicConfigPropertyDef> propertyIndex;
    private Set<String> acceptedPropertyNames;

    @Inject
    DynamicConfigPropertyList properties;
    @Inject
    Config config;

    private boolean isFiltered;

    /**
     * Constructor.
     */
    public DynamicConfigPropertyIndexImpl() {
    }

    @PostConstruct
    void onInit() {
        indexProperties(properties.getDynamicConfigProperties());
    }

    void filterOnAccepted() {
        if (!isFiltered) {
            filter();
            isFiltered = true;
        }
    }

    private Map<String, DynamicConfigPropertyDef> getPropertyIndex() {
        return this.propertyIndex;
    }

    private void indexProperties(List<DynamicConfigPropertyDef> dynamicConfigProperties) {
        this.propertyIndex = new HashMap<>(dynamicConfigProperties.size());
        for (DynamicConfigPropertyDef def : dynamicConfigProperties) {
            this.propertyIndex.put(def.getName(), def);
        }
    }

    private void filter() {
        this.acceptedPropertyNames = this.propertyIndex.entrySet().stream()
                .filter(entry -> accept(entry.getValue()))
                .map(entry -> entry.getKey()).collect(Collectors.toSet());
    }

    private boolean accept(DynamicConfigPropertyDef def) {
        if (def.getRequires() == null) {
            return true;
        }
        String[] requires = def.getRequires();
        for (String require : requires) {
            String requiredPropertyName = require;
            String requiredPropertyValue = null;
            if (require.contains("=")) {
                requiredPropertyName = require.substring(0, require.indexOf("=")).trim();
                requiredPropertyValue = require.substring(require.indexOf("=") + 1).trim();
            }
            Optional<String> actualPropertyValue = config.getOptionalValue(requiredPropertyName, String.class);
            if (actualPropertyValue.isEmpty() || !requiredPropertyValue.equals(actualPropertyValue.get())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigPropertyIndex#getProperty(java.lang.String)
     */
    @Override
    public DynamicConfigPropertyDef getProperty(String name) {
        return getPropertyIndex().get(name);
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigPropertyIndex#hasProperty(java.lang.String)
     */
    @Override
    public boolean hasProperty(String name) {
        return getPropertyIndex().containsKey(name);
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigPropertyIndex#getPropertyNames()
     */
    @Override
    public Set<String> getPropertyNames() {
        return getPropertyIndex().keySet();
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigPropertyIndex#isAccepted(java.lang.String)
     */
    @Override
    public boolean isAccepted(String propertyName) {
        return this.acceptedPropertyNames.contains(propertyName);
    }

    /**
     * @see io.apicurio.common.apps.config.DynamicConfigPropertyIndex#getAcceptedPropertyNames()
     */
    @Override
    public Set<String> getAcceptedPropertyNames() {
        filterOnAccepted();
        return this.acceptedPropertyNames;
    }

}
