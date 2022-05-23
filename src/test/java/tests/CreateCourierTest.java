package tests;

import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

import static steps.Steps.*;

public class CreateCourierTest {

  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Check create courier of /api/v1/courier")
  public void createCourier() {
    String login = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String firstName = RandomStringUtils.randomAlphabetic(10);

    Courier courier = new Courier(login, password, firstName);
    sendPostRequestAndGetResponse(courier, "/api/v1/courier");

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
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

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
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

    CourierCredentials courierCredentials = new CourierCredentials(login, password);
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