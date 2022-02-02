package apicurio.common.app.components.config.index.deployment;

import apicurio.common.app.components.config.index.DynamicConfigPropertyDto;
import apicurio.common.app.components.config.index.DynamicPropertiesInfo;
import apicurio.common.app.components.config.index.DynamicPropertiesInfoRecorder;
import io.quarkus.arc.deployment.BeanDiscoveryFinishedBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.processor.InjectionPointInfo;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.runtime.RuntimeValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.jandex.DotName;

import java.util.List;
import java.util.stream.Collectors;

class ConfigIndexProcessor {

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void syntheticBean(DynamicPropertiesInfoRecorder recorder, BeanDiscoveryFinishedBuildItem beanDiscovery, BuildProducer<SyntheticBeanBuildItem> syntheticBeans) {

        List<DynamicConfigPropertyDto> dynamicProperties = beanDiscovery.getInjectionPoints()
                .stream()
                .filter(ConfigIndexProcessor::isDynamicConfigProperty)
                .map(injectionPointInfo -> {
                    final DynamicConfigPropertyDto dynamicConfigPropertyDto = new DynamicConfigPropertyDto();
                    dynamicConfigPropertyDto.setName(injectionPointInfo.getTargetInfo());
                    return dynamicConfigPropertyDto;
                })
                .collect(Collectors.toList());

        final RuntimeValue<DynamicPropertiesInfo> dynamicPropertiesHolderRuntimeValue = recorder.initializePropertiesInfo(
                dynamicProperties);

        syntheticBeans.produce(SyntheticBeanBuildItem.configure(DynamicPropertiesInfo.class)
                .runtimeValue(dynamicPropertiesHolderRuntimeValue)
                .unremovable()
                .setRuntimeInit()
                .done());
    }

    private static boolean isDynamicConfigProperty(InjectionPointInfo injectionPointInfo) {
        return injectionPointInfo.getRequiredQualifier(DotName.createSimple(ConfigProperty.class.getName())) != null && injectionPointInfo.getTarget().asField().annotation(DotName.createSimple("io.apicurio.common.apps.config.Dynamic")) != null;
    }
}
