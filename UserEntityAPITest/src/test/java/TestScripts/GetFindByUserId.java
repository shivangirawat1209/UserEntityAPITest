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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = UserEntityApiTestApplication.class)
public class GetFindByUserId extends AbstractTestNGSpringContextTests {
    @Value("${dob}")
    private String dob;
    @Value("${firstname}")
    private String firstName;
    @Value("${lastname}")
    private String lastName;
    @Value("${email}")
    private String email;
    @Value("${getid}")
    private String getId;

    private int id;

    @BeforeMethod
    public void setURI() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void tc01_Verify_Success_Response_Code_GetApi() {

        Response response =
                given().pathParam("id", getId).when().get("api/users/{id}")
                        .then().log().body().assertThat().statusCode(200).extract().response();


    }

    @Test
    public void tc02_Verify_Error_Response_GetApi_When_Invalid_Id_Is_Sent() {

        Response response =
                given().pathParam("id", "NULL").when().get("api/users/{id}")
                        .then().log().body().assertThat().statusCode(400).extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("status"), "BAD_REQUEST");

    }

    @Test
    public void tc03_Verify_User_Info_In_Get_Api_Response_After_Adding_New_User() {
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

            JsonPath jsonPath = new JsonPath(response.asString());
            id = jsonPath.get("id");

            Response getResponse =
                    given().pathParam("id", id).when().get("api/users/{id}")
                            .then().log().body().assertThat().statusCode(200).extract().response();

            jsonPath = new JsonPath(getResponse.asString());
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
        }
    }

    @AfterMethod
    public void tearDown() {
        given().pathParam("id", id).when().delete("api/users/{id}");

    }
}
