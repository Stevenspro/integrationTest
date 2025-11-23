package de.club.integrationtest.citrus;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

public class TestCreateMultipleRequest extends TestSupport {


    @CitrusTest
    public void createMultipleUserRequest() {
        author("Steve");
        description("""
                create multiple Request""");

        createRequest(  "Bescheinigung Teilnahme English Kurs","","1","1");
        createRequest(  "Bescheinigung Teilnahme Französich Kurs","","1","1");
        createRequest(  "Bescheinigung Teilnahme Deutsch Kurs","","1","1");
        createRequest(  "Bescheinigung Teilnahme Maths Kurs","","1","1");

    }

    private void createRequest(String title, String description,
                               String userId, String managerId) {
        run(createVariable("title", title));
        run(createVariable("description", description));
        run(createVariable("userId", userId));
        run(createVariable("managerId", managerId));


        run(http().client("requestHttpClient").send().post()
                .message().type(MessageType.JSON)

                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addRequest.json"))
                .process(jsonPath()
                        .expression("$.description", "${description}")
                        .expression("$.title", "${title}")
                        .expression("$.userId", "${userId}")
                        .expression("$.managerId", "${managerId}")
                ));

        run(http().client("requestHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));

    }
}
