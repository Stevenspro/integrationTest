package de.club.integrationtest.citrus;

import de.club.integrationtest.ddf.TestSupport;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

public class TestCreateWorkGroup extends TestSupport {

    @CitrusTest
    public void createMultipleWorkgroup() {
        author("Steve");
        description("""
                create multiple Workgroups""");

        createWorkgroup("Workgroup Programmierung","Java Programmiersprache lernen",2,"2026-01-01","Essen",236f,1);
        createWorkgroup("Workgroup cruise","cross lernen",1,"2026-04-21","Düsseldorf",56f,1);
        createWorkgroup("Workgroup Französich ","Französich lernen",4,"2026-05-25","Dortmund",43f,1);
        createWorkgroup("Workgroup Deusche","Deusche lernen",4,"2026-05-25","Dortmund",43f,2);
        createWorkgroup("Workgroup test ","Deusche lernen",4,"2026-05-25","Dortmund",40f,1);
        createWorkgroup("Workgroup test1 ","Deusche für anfänger lernen",4,"2026-05-25","Dortmund",13f,2);

    }

    private void createWorkgroup(String title, String description, int duration, String date, String location, float price, long creatorId) {
        run(createVariable("description", description));
        run(createVariable("title", title));
        run(createVariable("duration", duration));
        run(createVariable("date", date));
        run(createVariable("location", location));
        run(createVariable("price", price));
        run(createVariable("creatorId", creatorId));

        run(http().client("workgroupHttpClient").send().post()
                .message().type(MessageType.JSON)
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addWorkgroup.json"))
                .process(jsonPath()
                        .expression("$.description", "${description}")
                        .expression("$.title", "${title}")
                        .expression("$.duration", "${duration}")
                        .expression("$.date", "${date}")
                        .expression("$.location", "${location}")
                        .expression("$.price", "${price}")
                        .expression("$.creatorId", "${creatorId}")
                ));

        run(http().client("workgroupHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));

    }
}
