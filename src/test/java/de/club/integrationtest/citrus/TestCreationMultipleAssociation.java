package de.club.integrationtest.citrus;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

public class TestCreationMultipleAssociation extends TestSupport {


    //http://localhost:8080/club/association/create?associationName=vkii&organisationId=1&creatorId=3
    @CitrusTest
    public void createMultipleUseAssociation() {
        author("Steve");
        description("""
                create multiple Association""");

        createAssociation("VKII-ruhrgebiet", "dortmundstr", "Dortmund", "Germany", 23, 45138, 1);
        createAssociation("SekteurEssos", "mainzstr", "Mainz", "Germany", 26, 65132, 3);
        createAssociation("SekteurA", "Apollo Bar", "Yaounde", "Cameroon", 2643, 6582, 2);
    }

    private void createAssociation(String associationName, String streetName,
                                   String city, String country, int streetNumber, int zipCode, int organisationId) {
        run(createVariable("associationName", associationName));
        run(createVariable("streetName", streetName));
        run(createVariable("city", city));
        run(createVariable("country", country));
        run(createVariable("streetNumber", streetNumber));
        run(createVariable("zipCode", zipCode));
        run(createVariable("organisationId", organisationId));

        run(http().client("associationHttpClient").send().post()
                .message().type(MessageType.JSON)
                .queryParam("associationName", "${associationName}")
                .queryParam("organisationId", "${organisationId}")
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

        run(http().client("associationHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));

    }
}
