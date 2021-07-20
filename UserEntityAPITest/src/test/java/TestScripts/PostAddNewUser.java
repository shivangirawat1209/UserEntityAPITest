package TestScripts;

import RestPojoClasses.AddUser;
import RestPojoClasses.Content;
import RestPojoClasses.Links;
import RestPojoClasses.PostResponsePayload;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.UserEntityAPITest.UserEntityApiTestApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;


@SpringBootTest(classes = UserEntityApiTestApplication.class)
public class PostAddNewUser extends AbstractTestNGSpringContextTests {

    @Value("${dob}")
    private String dob;
    @Value("${firstname}")
    private String firstName;
    @Value("${lastname}")
    private String lastName;
    @Value("${email}")
    private String email;

    private int id;

    @BeforeMethod
    public void setURI() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void tc01_Verify_Success_Response_On_Adding_New_User() {
        {
            AddUser addUser = new AddUser();
            addUser.setDayOfBirth(dob);
            addUser.setEmail(email);
            addUser.setFirstName(firstName);
            addUser.setLastName(lastName);

            Response response = given().contentType(ContentType.JSON)
                    .body(addUser).when().post("api/users").then().log().body().extract().response();

            JsonPath jsonPath = new JsonPath(response.asString());
            id = jsonPath.get("id");
            Assert.assertEquals(response.statusCode(), 201);

        }
    }

    @Test
    public void tc02_Verify_User_Info_In_Post_Response_Matches_Request() {
        {
            AddUser addUser = new AddUser();
            addUser.setDayOfBirth(dob);
            addUser.setEmail(email);
            addUser.setFirstName(firstName);
            addUser.setLastName(lastName);

            Response response =
                    given().contentType(ContentType.JSON)
                            .body(addUser).when().post("api/users").then().log().body().assertThat().statusCode(201)
                            .extract().response();
            try {
                JsonPath jsonPath = new JsonPath(response.asString());
                id = jsonPath.get("id");
                String firstName = jsonPath.getString("firstName");
                String lastName = jsonPath.getString("lastName");
                String email = jsonPath.getString("email");
                String dayOfBirth = jsonPath.getString("dayOfBirth");

                AddUser newUser = new AddUser();
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setEmail(email);
                newUser.setDayOfBirth(dayOfBirth);

                Assert.assertEquals(addUser.toString(), newUser.toString());
            } catch (Exception e) {
                Assert.fail("Response Schema not as expected, Exception occurred:" + e);
            }
        }
    }

    @Test
    public void tc03_Verify_Response_Schema_For_Post_API() throws IOException {
        {
            AddUser addUser = new AddUser();
            addUser.setDayOfBirth(dob);
            addUser.setEmail(email);
            addUser.setFirstName(firstName);
            addUser.setLastName(lastName);

            Response response = given().log().body().contentType(ContentType.JSON)
                    .body(addUser).when().post("api/users").then().log().body().extract().response();

            try {
                JsonPath jsonPath = new JsonPath(response.asString());
                id = jsonPath.get("id");
                PostResponsePayload postResponsePayload = new PostResponsePayload();
                postResponsePayload.setId(jsonPath.get("id"));
                postResponsePayload.setFirstName(jsonPath.getString("firstName"));
                postResponsePayload.setLastName(jsonPath.getString("lastName"));
                postResponsePayload.setEmail(jsonPath.getString("email"));
                postResponsePayload.setDayOfBirth(jsonPath.getString("dayOfBirth"));

                ArrayList<Content> contentList = new ArrayList<>();
                postResponsePayload.setContent(contentList);

                Links links1 = new Links();
                links1.setRel(jsonPath.getString("links[0].rel"));
                links1.setHref(jsonPath.getString("links[0].href"));

                Links links2 = new Links();
                links2.setRel(jsonPath.getString("links[1].rel"));
                links2.setHref(jsonPath.getString("links[1].href"));

                ArrayList<Links> links = new ArrayList<>();
                links.add(links1);
                links.add(links2);

                postResponsePayload.setLinks(links);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                String postResponseSchemaCheck = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postResponsePayload);

                Assert.assertEquals(response.asString(), postResponseSchemaCheck);
            } catch (Exception e) {
                Assert.fail("Response Schema not as expected, Exception occurred:" + e);
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        given().pathParam("id", id).when().delete("api/users/{id}");

    }
}
