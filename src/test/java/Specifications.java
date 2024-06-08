import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class Specifications {

    public static ValidatableResponse getRequest(String url) {
        return RestAssured.given()
                .baseUri(TestData.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get(url).then();
    }

    public static ValidatableResponse postRequest(Object object, String url) {
        return RestAssured.given()
                .baseUri(TestData.BASE_URL)
                .contentType(ContentType.JSON)
                .body(object)
                .when()
                .post(url).then();
    }

    public static ValidatableResponse deleteRequest(String url) {
        return RestAssured.given()
                .baseUri(TestData.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .delete(url).then();
    }

}
