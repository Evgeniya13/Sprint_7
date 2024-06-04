import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)

public class OrderTest {
    private final String[] color;

    public OrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] courierData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{}},
        };
    }

    @Before
    @Step("Send base URL")
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create order")
    @Step("Compare status of response and trackId of order")
    public void createOrder() {
        Order order = new Order("Тест", "Тестов", "Цветочная улица, д.5", 4,
                "+79161234567", 5, "2024-12-12", "Тест", color);
        int trackId = postCreateOrder(order).then().assertThat().statusCode(201)
                .extract()
                .path("track");
        Assert.assertNotNull(trackId);
    }

    @Step("Send POST request /api/v1/orders")
    private Response postCreateOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}