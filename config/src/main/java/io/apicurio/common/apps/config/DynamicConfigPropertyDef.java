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

import java.util.Objects;

/**
 * @author eric.wittmann@gmail.com
 */
public class DynamicConfigPropertyDef<T> {

    private final String name;
    private final Class<T> type;

    /**
     * Constructor.
     * @param name the config property name
     * @param defaultValue the default value of the config property
     */
    public DynamicConfigPropertyDef(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the name
     */
    public String name() {
        return name;
    }

    /**
     * @return the type
     */
    public Class<T> type() {
        return type;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynamicConfigProperty [name=" + name + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DynamicConfigPropertyDef other = (DynamicConfigPropertyDef) obj;
        return Objects.equals(name, other.name);
    }

}
