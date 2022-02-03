package io.apicurio.common.apps.config;


import java.util.List;

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
