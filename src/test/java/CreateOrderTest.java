import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@RunWith(Parameterized.class)
public class CreateOrderTest {
  private final String firstName;
  private final String lastName;
  private final String address;
  private final Integer metroStation;
  private final String phone;
  private final Integer rentTime;
  private final String deliveryDate;
  private final String comment;
  private final String[] color;

  public CreateOrderTest(String firstName, String lastName, String address, Integer metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.metroStation = metroStation;
    this.phone = phone;
    this.rentTime = rentTime;
    this.deliveryDate = deliveryDate;
    this.comment = comment;
    this.color = color;
  }

  @Parameterized.Parameters
  public static Object[][] getOrderParams() {
    return new Object[][] {
            {
              "Naruto",
              "Uchiha",
              "Konoha, 142 apt.",
              4,
              "+7 800 355 35 35",
              5,
              "2020-06-06",
              "Saske, come back to Konoha",
              new String[]{"BLACK"}
            },
            {
              "Naruto",
              "Uchiha",
              "Konoha, 142 apt.",
              4,
              "+7 800 355 35 35",
              5,
              "2020-06-06",
              "Saske, come back to Konoha",
              new String[]{"BLACK", "GREY"}
            },
            {
              "Naruto",
              "Uchiha",
              "Konoha, 142 apt.",
              4,
              "+7 800 355 35 35",
              5,
              "2020-06-06",
              "Saske, come back to Konoha",
              new String[]{}
            }
    };
  }

  static class Order {
    public final String firstName;
    public final String lastName;
    public final String address;
    public final Integer metroStation;
    public final String phone;
    public final Integer rentTime;
    public final String deliveryDate;
    public final String comment;
    public final String[] color;


    Order(String firstName, String lastName, String address, Integer metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.metroStation = metroStation;
      this.phone = phone;
      this.rentTime = rentTime;
      this.deliveryDate = deliveryDate;
      this.comment = comment;
      this.color = color;
    }
  }

  static class CancelOrder {
    public final Integer track;


    CancelOrder(Integer track) {
      this.track = track;
    }
  }

  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
  }

  @Step("Send post request and get response '{url}'")
  public Response sendPostRequestAndGetResponse(Object body, String url) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(body)
            .when()
            .post(url);
  }

  @Step("Cancel order")
  public void cancelOrder(Object body) {
    Response response = given()
            .header("Content-type", "application/json")
            .and()
            .body(body)
            .when()
            .put("/api/v1/orders/cancel");
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

  @Step("Check JSONPath")
  public void checkJsonPath(Response response, String jsonPath) {
    response.then().assertThat().body("$", hasKey(jsonPath));
  }

  @Test
  @DisplayName("Create orders of /api/v1/orders")
  public void createOrders() {
    Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    Response response = sendPostRequestAndGetResponse(order, "/api/v1/orders");
    checkStatusCode(response, 201);
    checkJsonPath(response, "track");

    Integer track = response.path("track");
    cancelOrder(new CancelOrder(track));
  }
}
