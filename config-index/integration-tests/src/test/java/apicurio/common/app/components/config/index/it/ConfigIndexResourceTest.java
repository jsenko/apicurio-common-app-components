package apicurio.common.app.components.config.index.it;

import apicurio.common.app.components.config.index.DynamicPropertiesInfo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ConfigIndexResourceTest {

    @Test
    public void testGetDynamicConfigInfo() {
        final DynamicPropertiesInfo dynamicPropertiesInfo = given()
                .when().get("/config-index").as(DynamicPropertiesInfo.class);

        Assert.assertTrue(dynamicPropertiesInfo.getDynamicConfigProperties().size() == 4);
    }
}
