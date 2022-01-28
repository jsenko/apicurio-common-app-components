package apicurio.common.app.components.config.index;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.List;

@Recorder
public class DynamicPropertiesInfoRecorder {

    public RuntimeValue<DynamicPropertiesInfo> initializePropertiesInfo(
            List<DynamicConfigPropertyDto> dynamicProperties) {

        return new RuntimeValue<>(new DynamicPropertiesInfo(dynamicProperties));
    }
}
