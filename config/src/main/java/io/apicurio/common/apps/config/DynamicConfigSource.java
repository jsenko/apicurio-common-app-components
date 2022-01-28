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

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * A microprofile-config configsource.  This class uses the dynamic config storage to
 * read/write configuration properties to, for example, a database.
 * <p>
 * TODO cache properties.  this would need to be multi-tenant aware?
 *
 * @author eric.wittmann@gmail.com
 */
public class DynamicConfigSource implements ConfigSource {

    private static DynamicConfigStorage storage;

    public static void setStorage(DynamicConfigStorage configStorage) {
        storage = configStorage;
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
        if (storage != null) {
            return storage.getConfigProperties().stream().map(cp -> cp.getName()).collect(Collectors.toSet());
        } else {
            //Not initialized yet
            return Collections.emptySet();
        }
    }

    /**
     * @see org.eclipse.microprofile.config.spi.ConfigSource#getValue(java.lang.String)
     */
    @Override
    public String getValue(String propertyName) {
        if (storage != null) {
            return storage.getConfigProperty(propertyName).getValue();
        } else {
            //Not initialized yet
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
