import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    @Before
    @Step("Create some orders before test")
    public void createOrders() {
        Order order1 = new Order("Ivan", "Ivanov", "Konoha, 142 apt.", 4,
                "+7 800 355 35 35", 5, "2020-06-06", "Saske," +
                "come back to Konoha", new String[]{"BLACK"});

        Order order2 = new Order("Petr", "Petrov", "Konoha, 142 apt.", 4,
                "+7 800 355 35 35", 5, "2020-06-06", "Saske," +
                "come back to Konoha", new String[]{"BLACK", "GREY"});

        Order order3 = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4,
                "+7 800 355 35 35", 5, "2020-06-06", "Saske," +
                "come back to Konoha", new String[]{});

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check list of orders")
    @Step("Compare status of response and order is not empty")
    public void checkOrderList() {
        getOrder().then().assertThat().statusCode(200);
        MatcherAssert.assertThat(getOrder(), notNullValue());
    }

    @Step("Send GET request to /api/v1/orders")
    private Response getOrder() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }
}


