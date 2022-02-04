package io.apicurio.common.apps.config;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicConfigPropertyIndex {

    private List<DynamicConfigPropertyDef> dynamicConfigProperties;
    private Map<String, DynamicConfigPropertyDef> propertyIndex;

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
        this.indexProperties(dynamicConfigProperties);
    }

    private void indexProperties(List<DynamicConfigPropertyDef> dynamicConfigProperties) {
        this.propertyIndex = new HashMap<>(dynamicConfigProperties.size());
        for (DynamicConfigPropertyDef def : dynamicConfigProperties) {
            this.propertyIndex.put(def.getName(), def);
        }
    }

    public DynamicConfigPropertyDef getProperty(String name) {
        return this.propertyIndex.get(name);
    }

    public boolean hasProperty(String name) {
        return this.propertyIndex.containsKey(name);
    }

}
