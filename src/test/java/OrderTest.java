import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
                {new String[]{"GREY", "BLACK"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Create order")
    @Step("Compare status of response and trackId of order")
    public void createOrder() {
        Order order = new Order("Тест", "Тестов", "Цветочная улица, д.5", 4,
                "+79161234567", 5, "2024-12-12", "Тест", color);

        int trackId = Specifications.postRequest(order, TestData.CREATE_ORDER).assertThat().statusCode(201)
                .extract()
                .path("track");
        Assert.assertNotNull(trackId);
    }
}