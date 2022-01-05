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
public class DynamicConfigPropertyDto {

    public static <T> DynamicConfigPropertyDto create(String name, T value) {
        if (value == null) {
            return new DynamicConfigPropertyDto(name, null, String.class.getName());
        } else {
            return new DynamicConfigPropertyDto(name, value.toString(), value.getClass().getName());
        }
    }

    private String name;
    private String value;
    private String type;

    /**
     * Constructor.
     */
    public DynamicConfigPropertyDto() {
    }

    /**
     * Constructor.
     * @param name the name of the property
     * @param value the value of the property
     * @param type the property type
     */
    public DynamicConfigPropertyDto(String name, String value, String type) {
        super();
        this.name = name;
        this.value = value;
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynamicConfigPropertyDto [name=" + name + ", value=" + value + ", type=" + type + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DynamicConfigPropertyDto other = (DynamicConfigPropertyDto) obj;
        return Objects.equals(name, other.name) && Objects.equals(type, other.type)
                && Objects.equals(value, other.value);
    }

}
