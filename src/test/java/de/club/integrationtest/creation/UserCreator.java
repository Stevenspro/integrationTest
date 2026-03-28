package de.club.integrationtest.creation;

import org.citrusframework.TestActionRunner;
import org.citrusframework.http.security.User;
import org.citrusframework.message.MessageType;
import org.citrusframework.spi.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.citrusframework.actions.CreateVariablesAction.Builder.createVariable;
import static org.citrusframework.http.actions.HttpActionBuilder.http;
import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static org.citrusframework.validation.xml.XpathMessageValidationContext.Builder.xpath;

@Component
public class UserCreator {


    public void createUser(TestActionRunner runner) {

        // createUser(runner, "Marie-bilge", "Black", "black@gms.de", 3);
    }

    public void createUser(TestActionRunner runner, String firstName, String lastName, String email, int userRole, long creatorId) {

        runner.run(createVariable("firstName", firstName));
        runner.run(createVariable("lastName", lastName));
        runner.run(createVariable("email", email));
        runner.run(createVariable("userRole", userRole));
        runner.run(createVariable("creatorId", creatorId));

        runner.run(http().client("helloHttpClient").send().post()
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

        runner.run(http().client("helloHttpClient").receive().response(HttpStatus.OK)
                .validate(xpath().schemaValidation(false)));
    }
}
