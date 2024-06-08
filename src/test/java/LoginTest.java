import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    private final static String COURIER_LOGIN = "TestCourier" + Math.random();
    private final static String PASSWORD = "1234";
    private final static String COURIER_NAME = "Test Courier";
    private final static String NOT_EXIST_LOGIN = "Bla-Bla-Bla";
    private static int userId = 0;

    @BeforeClass
    @Step("Create user before test")
    public static void createTestCourier() {
        Specifications.postRequest(new Courier(COURIER_LOGIN, PASSWORD, COURIER_NAME), TestData.CREATE_COURIER)
                .statusCode(201);
    }

    @Test
    @DisplayName("Success login")
    @Step("Compare status of response to 200")
    public void successfulLogin() {
        userId = Specifications.postRequest(new Login(COURIER_LOGIN, PASSWORD), TestData.LOGIN_COURIER).assertThat()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("User not found")
    @Step("Compare message and status of response")
    public void userDataNotFound() {
        Specifications.postRequest(new Login(NOT_EXIST_LOGIN, PASSWORD), TestData.LOGIN_COURIER)
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Login without login")
    @Step("Compare message and status of response")
    public void loginWithoutLogin() {
        Specifications.postRequest(new Login("", PASSWORD), TestData.LOGIN_COURIER)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @AfterClass
    @DisplayName("Delete created user")
    @Step("Check and delete user if userID != 0")
    public static void deleteData() {
        if (userId != 0) {
            Specifications.deleteRequest(TestData.DELETE_COURIER + userId)
                    .assertThat().statusCode(200);
            userId = 0;
        }
    }
}


