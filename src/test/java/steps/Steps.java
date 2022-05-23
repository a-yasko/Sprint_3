package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class Steps {
  @Step("Send post request and get response '{url}'")
  public static Response sendPostRequestAndGetResponse(Object body, String url) {
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(body)
                    .when()
                    .post(url);

    return response;
  }

  @Step("Delete courier with id: {id}")
  public static void deleteCourier(Object id) {
    Response response = given().delete("/api/v1/courier/" + id);
    checkStatusCode(response, 200);
    checkResponseBody(response, "ok", true);
  }

  @Step("Check status code")
  public static void checkStatusCode(Response response, Integer statusCode) {
    response.then().assertThat().statusCode(statusCode);
  }

  @Step("Check response body")
  public static void checkResponseBody(Response response, String key, Object value) {
    response.then().assertThat().body(key, equalTo(value));
  }

  @Step("Check JSONPath")
  public static void checkJsonPath(Response response, String jsonPath) {
    response.then().assertThat().body("$", hasKey(jsonPath));
  }

  @Step("Cancel order")
  public static void cancelOrder(Object body) {
    Response response = given()
            .header("Content-type", "application/json")
            .and()
            .body(body)
            .when()
            .put("/api/v1/orders/cancel");
    checkStatusCode(response, 200);
    checkResponseBody(response, "ok", true);
  }
}
