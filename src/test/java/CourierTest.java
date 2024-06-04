import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)

public class CourierTest {
    private final Courier courier;
    private final int status;
    private final String message;

    public CourierTest(Courier courier, int status, String message) {
        this.courier = courier;
        this.status = status;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] courierData() {
        Random random = new Random();
        String randomLogin = "Zefirka" + random.nextInt(1000);
        return new Object[][]{
                {new Courier(randomLogin, "1234", "test"), 201, null},
                {new Courier(randomLogin, "1234", "test"), 409, "Этот логин уже используется. Попробуйте другой."},
                {new Courier(null, "1234", "test"), 400, "Недостаточно данных для создания учетной записи"},
                {new Courier("Master007", null, "test"), 400, "Недостаточно данных для создания учетной записи"},
                {new Courier(null, null, null), 400, "Недостаточно данных для создания учетной записи"},
        };
    }

    @Before
    @Step("Send base URL")
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create courier")
    @Step("Compare message and status of response")
    public void courierIsCreated() {
        if (message != null) {
            postCreateCourier(courier).then().assertThat().body("message", equalTo(message))
                    .and()
                    .statusCode(status);
        } else {
            postCreateCourier(courier).then().assertThat().body(equalTo("{\"ok\":true}"))
                    .and()
                    .statusCode(status);
        }
    }

    @Step("Send POST request to /api/v1/courier")
    private Response postCreateCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }
}
