package TestScripts;

import RestPojoClasses.AddUser;
import com.user.UserEntityAPITest.UserEntityApiTestApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = UserEntityApiTestApplication.class)
public class DeleteUser extends AbstractTestNGSpringContextTests {

    @Value("${dob}")
    private String dob;
    @Value("${firstname}")
    private String firstName;
    @Value("${lastname}")
    private String lastName;
    @Value("${email}")
    private String email;

    @BeforeMethod
    public void setURI() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void tc01_Verify_Success_Response_Code_For_Delete_Api() {
        {
            AddUser addUser = new AddUser();
            addUser.setDayOfBirth(dob);
            addUser.setEmail(email);
            addUser.setFirstName(firstName);
            addUser.setLastName(lastName);

            Response response =
                    given().contentType(ContentType.JSON)
                            .body(addUser).when().post("api/users").then().assertThat().statusCode(201)
                            .extract().response();

            JsonPath jsonPath = new JsonPath(response.asString());
            int id = jsonPath.get("id");

            Response deleteResponse =
                    given().pathParam("id", id).when().delete("api/users/{id}");
            int statusCode = deleteResponse.getStatusCode();

            Assert.assertEquals(statusCode, 204);
        }

    }

    @Test
    public void tc02_Verify_Error_Response_Code_For_Delete_Api_When_User_Does_Not_Exist() {
        {
            AddUser addUser = new AddUser();
            addUser.setDayOfBirth(dob);
            addUser.setEmail(email);
            addUser.setFirstName(firstName);
            addUser.setLastName(lastName);

            Response response =
                    given().contentType(ContentType.JSON)
                            .body(addUser).when().post("api/users").then().assertThat().statusCode(201)
                            .extract().response();

            JsonPath jsonPath = new JsonPath(response.asString());
            int id = jsonPath.get("id");


            given().pathParam("id", id).when().delete("api/users/{id}").then().log().status();

            Response deleteResponse = given().pathParam("id", id).when().delete("api/users/{id}");

            Assert.assertEquals(deleteResponse.getStatusCode(), 404);
        }

    }
}
