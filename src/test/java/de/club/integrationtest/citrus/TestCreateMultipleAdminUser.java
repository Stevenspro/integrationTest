package de.club.integrationtest.citrus;

import de.club.integrationtest.creation.UserCreator;
import de.club.integrationtest.ddf.TestSupport;
import jakarta.annotation.Resource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;

import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;


public class TestCreateMultipleAdminUser extends TestSupport {

    @Resource
    private UserCreator userCreator;

    @CitrusTest
    public void createMultipleUserWithDifferentRole() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Franck", "Herdt", "Herdt@dne.de", 3);
        createUser("Patrick", "Bauer", "Bauer@dne.de", 1);
        createUser("Mark", "Mann", "Mann@dne.de", 1);
        createUser("Ariel", "Parr", "Parr@dne.de", 0);
        createUser("Marcel", "Pils", "Pils@dne.de", 0);
    }

    private void createUser(String firstName, String lastName, String email, int userRole) {

        run(createVariable("firstName", firstName));
        run(createVariable("lastName", lastName));
        run(createVariable("email", email));
        run(createVariable("userRole", userRole));

        run(http().client("helloHttpClient").send().post()
                .message().type(MessageType.JSON)
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addUser.json"))
                .process(jsonPath()
                        .expression("$.firstName", "${firstName}")
                        .expression("$.lastName", "${lastName}")
                        .expression("$.email", "${email}")
                        .expression("$.userRole", "${userRole}")
                ));

        run(http().client("helloHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));
    }
}
