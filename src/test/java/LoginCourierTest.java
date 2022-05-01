import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class LoginCourierTest {
  static class CourierCredentials {
    public final String login;
    public final String password;


    CourierCredentials(String login, String password) {
      this.login = login;
      this.password = password;
    }
  }

  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
  }

  @Step("Send POST request and get response '{url}'")
  public Response sendPostRequestAndGetResponse(Object body, String url) {
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(body)
                    .when()
                    .post(url);

    return response;
  }

  @Step("Check status code")
  public void checkStatusCode(Response response, Integer statusCode) {
    response.then().assertThat().statusCode(statusCode);
  }

  @Step("Check response body")
  public void checkResponseBody(Response response, String key, Object value) {
    response.then().assertThat().body(key, equalTo(value));
  }

  @Step("Check JSONPath")
  public void checkJsonPath(Response response, String jsonPath) {
    response.then().assertThat().body("$", hasKey(jsonPath));
  }

  @Step("Delete courier with id: {id}")
  public void deleteCourier(Object id) {
    Response response = given().delete("/api/v1/courier/" + id);
    checkStatusCode(response, 200);
    checkResponseBody(response, "ok", true);
  }

  @Test
  @DisplayName("Log in of /api/v1/courier/login")
  public void checkLogin() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    CreateCourierTest.Courier courier = new CreateCourierTest.Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");

    Integer id = response.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Log in with all params of /api/v1/courier/login")
  public void loginWithAllParams() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    CreateCourierTest.Courier courier = new CreateCourierTest.Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 200);

    Integer id = response.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Log in with invalid params /api/v1/courier/login")
  public void loginWithInvalidParams() {
    CourierCredentials courierCredentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 404);
  }

  @Test
  @DisplayName("Log in without required param of /api/v1/courier/login")
  public void loginWithoutRequiredParam() {
    CourierCredentials courierCredentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), "");
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 400);
    checkResponseBody(response, "message", "Недостаточно данных для входа");
  }

  @Test
  @DisplayName("Log in with unexisting user of /api/v1/courier/login")
  public void loginWithInvalidParamsAndCheckError() {
    CourierCredentials courierCredentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 404);
    checkResponseBody(response, "message", "Учетная запись не найдена");
  }

  @Test
  @DisplayName("Check 'id' in response body of /api/v1/courier/login")
  public void logInCheckId() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    CreateCourierTest.Courier courier = new CreateCourierTest.Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 200);
    checkJsonPath(response, "id");

    Integer id = response.path("id");
    deleteCourier(id);
  }
}