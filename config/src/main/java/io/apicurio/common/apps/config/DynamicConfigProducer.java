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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * CDI producer used when injecting dynamic configuration properties into application injection
 * points.
 * @author eric.wittmann@gmail.com
 */
@ApplicationScoped
public class DynamicConfigProducer {

    @Inject
    DynamicConfigService configService;

    @Dependent
    @Produces
    @DynamicConfigProperty
    @SuppressWarnings("unchecked")
    protected <T> Supplier<T> produceSupplierConfigValue(InjectionPoint injectionPoint) {
        final DynamicConfigProperty dcp = injectionPoint.getAnnotated().getAnnotation(DynamicConfigProperty.class);
        String cpName = dcp.name();
        if (cpName.isBlank()) {
            throw new RuntimeException("No property name provided in @DynamicConfigProperty at " + injectionPoint.getMember().toString());
        }
        T defaultValue = defaultValueAs(dcp.defaultValue(), injectionPoint.getType());
        DynamicConfigPropertyDef<T> propertyDef = new DynamicConfigPropertyDef<T>(cpName, defaultValue);
        return () -> configService.get(propertyDef, (Class<T>) defaultValue.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <T> T defaultValueAs(String defaultValue, Type type) {
        if (defaultValue == DynamicConfigProperty.UNCONFIGURED_VALUE) {
            throw new RuntimeException("No default value provided for dynamic configuration property with type: " + type);
        }
        Class<?> rawType = getRawType(type);
        if (rawType.equals(String.class)) {
            return (T) defaultValueAsString(defaultValue);
        } else if (rawType.equals(Boolean.class)) {
            return (T) defaultValueAsBoolean(defaultValue);
        } else if (rawType.equals(Integer.class)) {
            return (T) defaultValueAsInteger(defaultValue);
        } else if (rawType.equals(Long.class)) {
            return (T) defaultValueAsLong(defaultValue);
        } else {
            throw new RuntimeException("Unsupported type for dynamic config property: " + type + " (must be a Supplier<T> where T is String|Long|Integer|Boolean");
        }
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] typeArgs = paramType.getActualTypeArguments();
            return rawTypeOf(typeArgs[0]);
        }
        throw new RuntimeException("Unsupported type for dynamic config property: " + type + " (must be a Supplier<T> where T is String|Long|Integer|Boolean");
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> rawTypeOf(final Type type) {
        if (type instanceof Class<?>) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            return rawTypeOf(((ParameterizedType) type).getRawType());
        } else {
            throw new RuntimeException("Unsupported type for dynamic config property: " + type + " (must be a Supplier<T> where T is String|Long|Integer|Boolean");
        }
    }

    private static String defaultValueAsString(String defaultValue) {
        return defaultValue;
    }

    private static Boolean defaultValueAsBoolean(String defaultValue) {
        return "true".equals(defaultValue);
    }

    private static Integer defaultValueAsInteger(String defaultValue) {
        return Integer.valueOf(defaultValue);
    }

    private static Long defaultValueAsLong(String defaultValue) {
        return Long.valueOf(defaultValue);
    }

}
