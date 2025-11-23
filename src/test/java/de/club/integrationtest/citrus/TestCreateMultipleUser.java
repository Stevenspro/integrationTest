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


public class TestCreateMultipleUser extends TestSupport {

    @Resource
    private UserCreator userCreator;

    @CitrusTest
    public void createMultipleSuperAdminUser() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Marcel", "Pils", "Pils@dne.de", 4, 1);
    }

    @CitrusTest
    public void createMultipleAdminUser() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Franck", "Herdt", "Herdt@dne.de", 3, 1);
    }

    @CitrusTest
    public void createMultipleManager() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Ariel", "Parr", "Parr@dne.de", 2, 1);
    }

    @CitrusTest
    public void createMultipleUser() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Patrick", "Bauer", "Bauer@dne.de", 1, 1);
        createUser("Mark", "Mann", "Mann@dne.de", 1, 1);
    }

    @CitrusTest
    public void createMultipleUserWithDifferentRole() {
        author("Steve");
        description("""
                create multiple users with different roles""");

        createUser("Franck", "Herdt", "Herdt@dne.de", 3, 1);
        createUser("Patrick", "Bauer", "Bauer@dne.de", 1, 1);
        createUser("Mark", "Mann", "Mann@dne.de", 1, 1);
        createUser("Ariel", "Parr", "Parr@dne.de", 2, 1);
        createUser("Marcel", "Pils", "Pils@dne.de", 4, 1);
    }

    private void createUser(String firstName, String lastName, String email, int userRole, long creatorId) {

        run(createVariable("firstName", firstName));
        run(createVariable("lastName", lastName));
        run(createVariable("email", email));
        run(createVariable("userRole", userRole));
        run(createVariable("creatorId", creatorId));

        run(http().client("helloHttpClient").send().post()
                .message().type(MessageType.JSON)
                .contentType("application/json")
                .body(Resources.fromClasspath("de/club/integrationtest/addUser.json"))
                .process(jsonPath()
                        .expression("$.firstName", "${firstName}")
                        .expression("$.lastName", "${lastName}")
                        .expression("$.email", "${email}")
                        .expression("$.userRole", "${userRole}")
                        .expression("$.creatorId", "${creatorId}")
                ));

        run(http().client("helloHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));
    }
}
