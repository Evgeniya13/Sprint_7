import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Before
    @Step("Create order")
    public void setUp() {
        Order order = new Order("Тест", "Тестов", "Цветочная улица, д.5", 4,
                "+79161234567", 5, "2024-12-12", "Тест", new String[]{"BLACK"});
        Specifications.postRequest(order, TestData.CREATE_ORDER).statusCode(201);
    }

    @Test
    @DisplayName("Check list of orders")
    public void checkOrderList() {
        Specifications.getRequest(TestData.GET_LIST_OF_ORDERS)
                .assertThat().statusCode(200).body("orders", notNullValue());
    }
}


