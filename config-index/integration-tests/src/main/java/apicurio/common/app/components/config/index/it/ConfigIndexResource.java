/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package apicurio.common.app.components.config.index.it;

import io.apicurio.common.apps.config.Dynamic;
import io.apicurio.common.apps.config.DynamicConfigPropertyIndex;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.function.Supplier;

@Path("/config-index")
@ApplicationScoped
public class ConfigIndexResource {
    // add some rest methods here

    @Inject
    DynamicConfigPropertyIndex dynamicPropertyIndex;

    @Dynamic
    @ConfigProperty(name = "app.properties.dynamic.string", defaultValue = "_DEFAULT_")
    Supplier<String> dynamicString;

    @Dynamic
    @ConfigProperty(name = "app.properties.dynamic.int", defaultValue = "0")
    Supplier<Integer> dynamicInt;

    @Dynamic
    @ConfigProperty(name = "app.properties.dynamic.long", defaultValue = "0")
    Supplier<Long> dynamicLong;

    @Dynamic
    @ConfigProperty(name = "app.properties.dynamic.bool", defaultValue = "false")
    Supplier<Boolean> dynamicBool;

    @ConfigProperty(name = "app.properties.static.string", defaultValue = "default-value")
    String staticString;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DynamicConfigPropertyIndex getDynamicPropertyIndex() {
        return dynamicPropertyIndex;
    }
}
