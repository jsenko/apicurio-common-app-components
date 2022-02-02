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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.apicurio.common.apps.config.DynamicConfigStorage;
import io.quarkus.runtime.Startup;

/**
 * @author eric.wittmann@gmail.com
 */
@ApplicationScoped
@Startup
public class DynamicConfigStartup {

    @Inject
    DynamicConfigStorage configStorage;

    @PostConstruct
    void onStart() {
        // TODO this doesn't work in practice when the config storage itself has config properties injected :(
//        DynamicConfigSource.setStorage(configStorage);
    }
}