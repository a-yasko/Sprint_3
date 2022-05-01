import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
  static class Courier {
    public final String login;
    public final String password;
    public final String firstName;


    Courier(String login, String password, String firstName) {
      this.login = login;
      this.password = password;
      this.firstName = firstName;
    }
  }

  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
  }

  @Step("Send post request and get response '{url}'")
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

  @Step("Delete courier with id: {id}")
  public void deleteCourier(Object id) {
    Response response = given().delete("/api/v1/courier/" + id);
    checkStatusCode(response, 200);
    checkResponseBody(response, "ok", true);
  }

  @Step("Check status code")
  public void checkStatusCode(Response response, Integer statusCode) {
    response.then().assertThat().statusCode(statusCode);
  }

  @Step("Check response body")
  public void checkResponseBody(Response response, String key, Object value) {
    response.then().assertThat().body(key, equalTo(value));
  }

  @Test
  @DisplayName("Check create courier of /api/v1/courier")
  public void createCourier() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    LoginCourierTest.CourierCredentials courierCredentials = new LoginCourierTest.CourierCredentials(login, password);
    Response logIn = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");

    Integer id = logIn.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Create existing courier of /api/v1/courier")
  public void createExistingCourier() {
    Courier courier = new Courier("ninja", "1234", "laLaLa");
    Response response = sendPostRequestAndGetResponse(courier, "/api/v1/courier");
    checkStatusCode(response, 409);
  }

  @Test
  @DisplayName("Create courier with all params of /api/v1/courier")
  public void createCourierWithAllParams() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
    Response response = sendPostRequestAndGetResponse(courier, "/api/v1/courier");
    checkStatusCode(response, 201);

    LoginCourierTest.CourierCredentials courierCredentials = new LoginCourierTest.CourierCredentials(login, password);
    Response logIn = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");

    Integer id = logIn.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Check success response of /api/v1/courier")
  public void checkSuccessResponse() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
    Response response = sendPostRequestAndGetResponse(courier, "/api/v1/courier");
    checkStatusCode(response, 201);
    checkResponseBody(response, "ok", true);

    LoginCourierTest.CourierCredentials courierCredentials = new LoginCourierTest.CourierCredentials(login, password);
    Response logIn = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");

    Integer id = logIn.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Create courier without required param of /api/v1/courier")
  public void createCourierWithoutRequiredParam() {
    Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10), "", RandomStringUtils.randomAlphabetic(10));
    Response response = sendPostRequestAndGetResponse(courier, "/api/v1/courier");
    checkStatusCode(response, 400);
    checkResponseBody(response, "message", "Недостаточно данных для создания учетной записи");
  }

  @Test
  @DisplayName("Create existing courier and check error of /api/v1/courier")
  public void createExistingCourierCheckError() {
    Courier courier = new Courier("ninja", "1234", "laLaLa");
    Response response = sendPostRequestAndGetResponse(courier, "/api/v1/courier");
    checkStatusCode(response, 409);
    checkResponseBody(response, "message", "Этот логин уже используется. Попробуйте другой.");
  }
}