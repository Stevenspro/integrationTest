package de.club.integrationtest.citrus;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

public class TestCreationMultipleOrganisation extends TestSupport {

    @CitrusTest
    public void createMultipleUseOrganisation() {
        author("Steve");
        description("""
                create multiple Organisation""");

        createOrganisation ("VKII","dortmundstr","Dortmund","Germany",23,45138, 1);
        createOrganisation ("Sekteur","mainzstr","Mainz","Germany",26,65132, 1);
        createOrganisation ("SekteurCMR","Apollo Bar","Yaounde","Cameroon",2643,6582, 1);
    }

    private void createOrganisation(String organisationName, String streetName,
                                    String city, String country, int streetNumber, int zipCode, long creatorId) {
        run(createVariable("organisationName", organisationName));
        run(createVariable("streetName", streetName));
        run(createVariable("city", city));
        run(createVariable("country", country));
        run(createVariable("streetNumber", streetNumber));
        run(createVariable("zipCode", zipCode));

        run(http().client("organisationHttpClient").send().post()
                .message().type(MessageType.JSON)
                .queryParam("name", "${organisationName}")
                .queryParam("creatorId", "1")
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addAddress.json"))
                .process(jsonPath()
                        .expression("$.streetName", "${streetName}")
                        .expression("$.city", "${city}")
                        .expression("$.country", "${country}")
                        .expression("$.streetNumber", "${streetNumber}")
                        .expression("$.zipCode", "${zipCode}")
                ));

        run(http().client("organisationHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));

    }

}
