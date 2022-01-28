package apicurio.common.app.components.config.index.it;

import io.apicurio.common.apps.config.DynamicConfigPropertyDto;
import io.apicurio.common.apps.config.DynamicConfigStorage;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class InMemoryConfigSource implements DynamicConfigStorage {

    private final HashMap<String, String> properties = new HashMap<>();

    @Override
    public DynamicConfigPropertyDto getConfigProperty(String propertyName) {
        return new DynamicConfigPropertyDto(propertyName, properties.get(propertyName));
    }

    @Override
    public void setConfigProperty(DynamicConfigPropertyDto propertyDto) {
        properties.put(propertyDto.getName(), propertyDto.getValue());
    }

    @Override
    public void deleteConfigProperty(String propertyName) {
        properties.remove(propertyName);
    }

    @Override
    public List<String> getTenantsWithStaleConfigProperties(Instant lastRefresh) {
        return null;
    }

    @Override
    public List<DynamicConfigPropertyDto> getConfigProperties() {
        return null;
    }
}
