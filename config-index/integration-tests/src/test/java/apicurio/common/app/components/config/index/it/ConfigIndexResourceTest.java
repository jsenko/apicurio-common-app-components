package apicurio.common.app.components.config.index.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ConfigIndexResourceTest {

    @Test
    public void testGetDynamicConfigInfo() {
        ConfigProps allProperties = given()
                .when().get("/config/all").as(ConfigProps.class);
        ConfigProps acceptedProperties = given()
                .when().get("/config/accepted").as(ConfigProps.class);

        Assertions.assertTrue(allProperties.getProperties().size() == 5);
        Assertions.assertTrue(acceptedProperties.getProperties().size() == 4);
        Assertions.assertNotNull(allProperties.getProperty("app.properties.dynamic.long"));
        Assertions.assertEquals("app.properties.dynamic.long", allProperties.getProperty("app.properties.dynamic.long").getName());
        Assertions.assertEquals("17", allProperties.getProperty("app.properties.dynamic.long").getValue());
        Assertions.assertTrue(allProperties.hasProperty("app.properties.dynamic.bool.dep"));
        Assertions.assertFalse(acceptedProperties.hasProperty("app.properties.dynamic.bool.dep"));
        Assertions.assertFalse(acceptedProperties.hasProperty("property.does.not.exist"));

        ConfigProp booleanProp = given()
                .when().get("/config/all/app.properties.dynamic.bool").as(ConfigProp.class);
        Assertions.assertEquals("false", booleanProp.getValue());

        // Set the bool property to true
        given().when().get("/config/update");

        // Value should be true now
        booleanProp = given()
                .when().get("/config/all/app.properties.dynamic.bool").as(ConfigProp.class);
        Assertions.assertEquals("true", booleanProp.getValue());

        allProperties = given()
                .when().get("/config/all").as(ConfigProps.class);
        Assertions.assertTrue(allProperties.hasProperty("app.properties.dynamic.bool"));
        Assertions.assertEquals("true", allProperties.getPropertyValue("app.properties.dynamic.bool"));
        // Accepted properties should now have 5 items
        acceptedProperties = given()
                .when().get("/config/accepted").as(ConfigProps.class);
        Assertions.assertTrue(acceptedProperties.getProperties().size() == 5);
        Assertions.assertTrue(acceptedProperties.hasProperty("app.properties.dynamic.bool"));
        Assertions.assertEquals("true", acceptedProperties.getPropertyValue("app.properties.dynamic.bool"));
        Assertions.assertTrue(acceptedProperties.hasProperty("app.properties.dynamic.bool.dep"));
        Assertions.assertFalse(acceptedProperties.hasProperty("property.does.not.exist"));
    }
}
