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

import java.util.Optional;

/**
 * @author eric.wittmann@gmail.com
 */
@SuppressWarnings("rawtypes")
public interface DynamicConfigService {

    public String get(DynamicConfigProperty property);
    public <T> T get(DynamicConfigProperty<T> property, Class<T> propertyType);
    public Optional<String> getOptional(DynamicConfigProperty property);
    public <T> Optional<T> getOptional(DynamicConfigProperty<T> property, Class<T> propertyType);

}
