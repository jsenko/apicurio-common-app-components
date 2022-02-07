package apicurio.common.app.components.config.index.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ConfigIndexResourceTest {

    @Test
    public void testGetDynamicConfigInfo() {
        final ConfigProps properties = given()
                .when().get("/config-index").as(ConfigProps.class);

        Assertions.assertTrue(properties.getProperties().size() == 4);

        Assertions.assertNotNull(properties.getProperty("app.properties.dynamic.long"));
        Assertions.assertEquals("app.properties.dynamic.long", properties.getProperty("app.properties.dynamic.long").getName());
        Assertions.assertEquals("17", properties.getProperty("app.properties.dynamic.long").getValue());
    }
}
