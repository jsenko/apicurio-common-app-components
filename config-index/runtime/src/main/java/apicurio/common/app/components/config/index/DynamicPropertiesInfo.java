package apicurio.common.app.components.config.index;


import java.util.List;

public class DynamicPropertiesInfo {

    private List<DynamicConfigPropertyDto> dynamicConfigProperties;

    public DynamicPropertiesInfo() {
    }

    public DynamicPropertiesInfo(List<DynamicConfigPropertyDto> dynamicConfigProperties) {
        this.dynamicConfigProperties = dynamicConfigProperties;
    }

    public List<DynamicConfigPropertyDto> getDynamicConfigProperties() {
        return dynamicConfigProperties;
    }

    public void setDynamicConfigProperties(List<DynamicConfigPropertyDto> dynamicConfigProperties) {
        this.dynamicConfigProperties = dynamicConfigProperties;
    }
}
