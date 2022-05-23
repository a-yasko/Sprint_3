package tests;

import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class OrderListTest {
  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Check order list of /api/v1/orders")
  public void checkOrderList() {
    given()
            .get("/api/v1/orders")
            .then()
            .assertThat()
            .body("orders[0]", hasKey("id"))
            .and()
            .statusCode(200);
  }
}
