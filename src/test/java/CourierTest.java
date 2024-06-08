import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)

public class CourierTest {
    private final Courier courier;
    private final int status;
    private final String message;
    private final static String COURIER_LOGIN = "TestCourier" + Math.random();
    private final static String PASSWORD = "1234";
    private static int userID = 0;

    public CourierTest(Courier courier, int status, String message) {
        this.courier = courier;
        this.status = status;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] courierData() {
        return new Object[][]{
                {new Courier(COURIER_LOGIN, PASSWORD, "test"), 201, null},
                {new Courier(COURIER_LOGIN, PASSWORD, "test"), 409, "Этот логин уже используется. Попробуйте другой."},
                {new Courier(null, "1234", "test"), 400, "Недостаточно данных для создания учетной записи"},
                {new Courier("Master007", null, "test"), 400, "Недостаточно данных для создания учетной записи"},
                {new Courier(null, null, null), 400, "Недостаточно данных для создания учетной записи"},
        };
    }

    @Test
    @DisplayName("Create courier")
    @Step("Compare message and status of response")
    public void courierIsCreated() {
        if (message != null) {
            Specifications.postRequest(courier, TestData.CREATE_COURIER)
                    .assertThat().body("message", equalTo(message))
                    .and()
                    .statusCode(status);
        } else {
            Specifications.postRequest(courier, TestData.CREATE_COURIER)
                    .assertThat().body(equalTo("{\"ok\":true}"))
                    .and()
                    .statusCode(status);
            if (status == 201) {
                userID = Specifications.postRequest(new Login(COURIER_LOGIN, PASSWORD), TestData.LOGIN_COURIER)
                        .statusCode(200)
                        .extract()
                        .path("id");
            }
        }
    }

    @AfterClass
    public static void deleteCourier() {
        if (userID != 0) {
            Specifications.deleteRequest(TestData.DELETE_COURIER + userID)
                    .assertThat().statusCode(200);
            userID = 0;
        }
    }
}

