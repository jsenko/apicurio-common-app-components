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

package apicurio.common.app.components.config.index;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author eric.wittmann@gmail.com
 */
public class DynamicConfigPropertyDto {

    private String name;
    private Type requiredType;

    /**
     * Constructor.
     */
    public DynamicConfigPropertyDto() {
    }

    /**
     * Constructor.
     *
     * @param name         the name of the property
     * @param requiredType the value of the requiredType
     */
    public DynamicConfigPropertyDto(String name, Type requiredType) {
        super();
        this.name = name;
        this.requiredType = requiredType;
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
    public Type getRequiredType() {
        return requiredType;
    }

    /**
     * @param value the requiredType to set
     */
    public void setRequiredType(Type value) {
        this.requiredType = requiredType;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "DynamicConfigPropertyDto [name=" + name + ", requiredType=" + requiredType + "]";
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, requiredType);
    }

    /**
     * @see Object#equals(Object)
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
        return Objects.equals(name, other.name) && Objects.equals(requiredType, other.requiredType);
    }

}
