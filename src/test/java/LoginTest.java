import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    private final static String COURIER_LOGIN = "TestCourier" + Math.random();
    private final static String PASSWORD = "1234";
    private final static String COURIER_NAME = "Test Courier";
    private static int userId = 0;

    @BeforeClass
    @Step("Create user before test")
    public static void createTestCourier() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        given()
                .header("Content-type", "application/json")
                .body(new Courier(COURIER_LOGIN, PASSWORD, COURIER_NAME))
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Success login")
    @Step("Compare status of response to 200")
    public void successfulLogin() {
        userId = postLoginCourier(COURIER_LOGIN, PASSWORD).then().assertThat()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("User not found")
    @Step("Compare message and status of response")
    public void userDataNotFound() {
        postLoginCourier("BlaBlaBla", "12345").then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Login without login")
    @Step("Compare message and status of response")
    public void loginWithoutLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"password\":\"" + PASSWORD + "\"}")
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @After
    @DisplayName("Delete created user")
    @Step("Check and delete user if userID != 0")
    public void deleteData() {
        if (userId != 0) {
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/" + userId).then().assertThat().statusCode(200);
            userId = 0;
        }
    }

    @Step("Send POST request to /api/v1/courier/login")
    private Response postLoginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .when()
                .post("/api/v1/courier/login");
    }
}


