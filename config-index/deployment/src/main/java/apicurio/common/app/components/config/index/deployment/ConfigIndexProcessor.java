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

package apicurio.common.app.components.config.index.deployment;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

import apicurio.common.app.components.config.index.DynamicPropertiesInfoRecorder;
import io.apicurio.common.apps.config.Dynamic;
import io.apicurio.common.apps.config.DynamicConfigPropertyDef;
import io.apicurio.common.apps.config.DynamicConfigPropertyIndex;
import io.quarkus.arc.deployment.BeanDiscoveryFinishedBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.processor.InjectionPointInfo;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.runtime.RuntimeValue;

class ConfigIndexProcessor {

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void syntheticBean(DynamicPropertiesInfoRecorder recorder, BeanDiscoveryFinishedBuildItem beanDiscovery, BuildProducer<SyntheticBeanBuildItem> syntheticBeans) {
        List<DynamicConfigPropertyDef> dynamicProperties = beanDiscovery.getInjectionPoints()
                .stream()
                .filter(ConfigIndexProcessor::isDynamicConfigProperty)
                .map(injectionPointInfo -> {
                    try {
                        AnnotationInstance ai = injectionPointInfo.getRequiredQualifier(DotName.createSimple(ConfigProperty.class.getName()));
                        Type supplierType = injectionPointInfo.getRequiredType();
                        Type actualType = supplierType.asParameterizedType().arguments().get(0);

                        final String propertyName = ai.value("name").asString();
                        final Class<?> propertyType = Class.forName(actualType.name().toString());
                        return new DynamicConfigPropertyDef(propertyName, propertyType);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        final RuntimeValue<DynamicConfigPropertyIndex> dynamicPropertiesHolderRuntimeValue = recorder.initializePropertiesInfo(
                dynamicProperties);

        syntheticBeans.produce(SyntheticBeanBuildItem.configure(DynamicConfigPropertyIndex.class)
                .runtimeValue(dynamicPropertiesHolderRuntimeValue)
                .unremovable()
                .setRuntimeInit()
                .done());
    }

    private static boolean isDynamicConfigProperty(InjectionPointInfo injectionPointInfo) {
        return injectionPointInfo.getRequiredQualifier(DotName.createSimple(ConfigProperty.class.getName())) != null &&
                injectionPointInfo.getTarget().asField().annotation(DotName.createSimple(Dynamic.class.getName())) != null;
    }
}
