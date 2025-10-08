package de.club.integrationtest;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.citrusframework.testng.CitrusParameters;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;


@Test
public class FirstRunTestCase extends TestSupport {


    @CitrusTest
    public void test() {
        description("First example showing the basic Java DSL!");
    }

    @CitrusTest(name = "Simple_IT")
    public void simpleTest() {
        description("First example showing the basic Java DSL!");
        run(createVariable("user", "Citrus"));
        run(echo().message("Hello ${user}!"));
    }

    @CitrusTest
    @CitrusParameters({"message", "delay"})
    @Test(dataProvider = "messageDataProvider")
    public void dataProvider(String message, Long sleep) {
        run(echo(message));
        run(sleep().milliseconds(sleep));
        run(echo("${message}"));
        run(echo("${delay}"));
    }

    @DataProvider
    public Object[][] messageDataProvider() {
        return new Object[][]{
                {"Hello World!", 300L},
                {"Citrus rocks!", 1000L},
                {"Hi from Citrus!", 500L},
        };
    }

    @CitrusTest
    public void sendMessageTest() {
        $(send("helloService")
                .message()
                .body(String.valueOf(new ClassPathResource("path/to/request.xml")))
        );
    }

    @CitrusTest
    public void testRequest() {
        run(http().client("helloHttpClient").send().post()
                .message().type(MessageType.JSON)
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addUser.json"))
                .process(jsonPath()
                        .expression("$.firstName", "")
                        .expression("$.lastName", "")
                        .expression("$.email", "test@club.de")
                        .expression("$.userRole", 1)
                ));

    }
}
