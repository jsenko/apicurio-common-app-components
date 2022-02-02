package apicurio.common.app.components.config.index;

import java.util.List;

import io.apicurio.common.apps.config.DynamicConfigPropertyDef;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DynamicPropertiesInfoRecorder {

    public RuntimeValue<DynamicConfigPropertyIndex> initializePropertiesInfo(
            List<DynamicConfigPropertyDef> dynamicProperties) {

        return new RuntimeValue<>(new DynamicConfigPropertyIndex(dynamicProperties));
    }
}
