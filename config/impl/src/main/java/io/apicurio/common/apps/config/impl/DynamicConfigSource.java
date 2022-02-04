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

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.spi.ConfigSource;

import io.apicurio.common.apps.config.DynamicConfigPropertyIndex;
import io.apicurio.common.apps.config.DynamicConfigStorage;

/**
 * A microprofile-config configsource.  This class uses the dynamic config storage to
 * read/write configuration properties to, for example, a database.
 * <p>
 * TODO cache properties.  this would need to be multi-tenant aware?  probably should be implemented in the storage layer
 *
 * @author eric.wittmann@gmail.com
 */
public class DynamicConfigSource implements ConfigSource {

    private static Optional<DynamicConfigStorage> storage = Optional.empty();
    public static void setStorage(DynamicConfigStorage configStorage) {
        storage = Optional.of(configStorage);
    }
    private static Optional<DynamicConfigPropertyIndex> configIndex = Optional.empty();
    public static void setConfigurationIndex(DynamicConfigPropertyIndex index) {
        configIndex = Optional.of(index);
    }

    @Override
    public int getOrdinal() {
        return 199;
    }

    /**
     * @see org.eclipse.microprofile.config.spi.ConfigSource#getPropertyNames()
     */
    @Override
    public Set<String> getPropertyNames() {
        if (storage.isPresent()) {
            return storage.get().getConfigProperties().stream().map(cp -> cp.getName()).collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * @see org.eclipse.microprofile.config.spi.ConfigSource#getValue(java.lang.String)
     */
    @Override
    public String getValue(String propertyName) {
        if (configIndex.isPresent() && configIndex.get().hasProperty(propertyName) && storage.isPresent()) {
            return storage.get().getConfigProperty(propertyName).getValue();
        } else {
            return null;
        }
    }

    /**
     * @see org.eclipse.microprofile.config.spi.ConfigSource#getName()
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
