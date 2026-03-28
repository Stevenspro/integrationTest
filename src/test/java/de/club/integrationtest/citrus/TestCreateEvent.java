package de.club.integrationtest.citrus;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

public class TestCreateEvent extends TestSupport {


    @CitrusTest
    public void createMultipleEvent() {
        author("Steve");
        description("""
                create multiple events""");

        createEvent("Programmierung kurse", "Ziel: Vielseitige Softwareentwicklung, von Android-Apps über Unternehmensanwendungen bis zu Web-Backends." +
                "Lernkurve: Gut für Einsteiger geeign", 2, "2026-01-01", "Essen", 236f, 1);

        createEvent("cruise kurse", "All-Inclusive Experience: Passengers typically pay for" +
                "Self-Contained Community: They are designed to provide everything a traveler needs", 1, "2026-04-21", "Düsseldorf", 56f, 1);

        createEvent("Französich kurse", "Französich lernen", 4, "2026-05-25", "Dortmund", 43f, 1);

        createEvent("Deusche kurse", "Deusche lernen", 4, "2026-05-25", "Dortmund", 43f, 2);
        createEvent("test kurse", "Deusche lernen", 4, "2026-05-25", "Dortmund", 40f, 1);
        createEvent("test1 kurse", "Deusche lernen", 4, "2026-05-25", "Dortmund", 13f, 2);

    }


    private void createEvent(String title, String description, int duration, String date, String location, float price, long organizer) {
        run(createVariable("description", description));
        run(createVariable("title", title));
        run(createVariable("duration", duration));
        run(createVariable("date", date));
        run(createVariable("location", location));
        run(createVariable("price", price));
        run(createVariable("organizer", organizer));

        run(http().client("eventHttpClient").send().post()
                .message().type(MessageType.JSON)
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addEvent.json"))
                .process(jsonPath()
                        .expression("$.description", "${description}")
                        .expression("$.title", "${title}")
                        //.expression("$.duration", "${duration}")
                        .expression("$.date", "${date}")
                        .expression("$.location", "${location}")
                        .expression("$.price", "${price}")
                        .expression("$.organizer", "${organizer}")
                ));

        run(http().client("eventHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));

    }
}
