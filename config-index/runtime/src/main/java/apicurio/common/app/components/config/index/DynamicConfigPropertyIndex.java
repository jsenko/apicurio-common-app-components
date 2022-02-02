package apicurio.common.app.components.config.index;


import java.util.List;

import io.apicurio.common.apps.config.DynamicConfigPropertyDef;

public class DynamicConfigPropertyIndex {

    private List<DynamicConfigPropertyDef> dynamicConfigProperties;

    public DynamicConfigPropertyIndex() {
    }

    public DynamicConfigPropertyIndex(List<DynamicConfigPropertyDef> dynamicConfigProperties) {
        this.setDynamicConfigProperties(dynamicConfigProperties);
    }

    public List<DynamicConfigPropertyDef> getDynamicConfigProperties() {
        return dynamicConfigProperties;
    }

    public void setDynamicConfigProperties(List<DynamicConfigPropertyDef> dynamicConfigProperties) {
        this.dynamicConfigProperties = dynamicConfigProperties;
    }

    public DynamicConfigPropertyDef getProperty(String name) {
        for (DynamicConfigPropertyDef def : dynamicConfigProperties) {
            if (def.getName().equals(name)) {
                return def;
            }
        }
        return null;
    }

}
