package tests;

import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static steps.Steps.*;

public class LoginCourierTest {

  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Log in of /api/v1/courier/login")
  public void checkLogin() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");

    Integer id = response.path("id");
    deleteCourier(id);
  }

  @Test
  @DisplayName("Log in with all required params of /api/v1/courier/login")
  public void loginWithAllParams() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
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
  @DisplayName("Log in without login of /api/v1/courier/login")
  public void loginWithoutLogin() {
    CourierCredentials courierCredentials = new CourierCredentials(null, RandomStringUtils.randomAlphabetic(10));
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 400);
    checkResponseBody(response, "message", "Недостаточно данных для входа");
  }

  @Test
  @DisplayName("Log in without password of /api/v1/courier/login")
  public void loginWithoutPassword() {
    CourierCredentials courierCredentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), null);
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

    Courier courier = new Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
    Response response = sendPostRequestAndGetResponse(courierCredentials, "/api/v1/courier/login");
    checkStatusCode(response, 200);
    checkJsonPath(response, "id");

    Integer id = response.path("id");
    deleteCourier(id);
  }
}