package apicurio.common.app.components.config.index.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.apicurio.common.apps.config.DynamicConfigPropertyIndex;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ConfigIndexResourceTest {

    @Test
    public void testGetDynamicConfigInfo() {
        final DynamicConfigPropertyIndex dynamicPropertiesInfo = given()
                .when().get("/config-index").as(DynamicConfigPropertyIndex.class);

        Assertions.assertTrue(dynamicPropertiesInfo.getDynamicConfigProperties().size() == 4);

        Assertions.assertNotNull(dynamicPropertiesInfo.getProperty("app.properties.dynamic.long"));
        Assertions.assertEquals("app.properties.dynamic.long", dynamicPropertiesInfo.getProperty("app.properties.dynamic.long").getName());
        Assertions.assertEquals(Long.class, dynamicPropertiesInfo.getProperty("app.properties.dynamic.long").getType());
    }
}
