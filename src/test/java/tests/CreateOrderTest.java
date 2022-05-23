package tests;

import client.BaseHttpClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CancelOrder;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static steps.Steps.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
  private final String[] color;

  public CreateOrderTest(String[] color) {
    this.color = color;
  }

  @Parameterized.Parameters
  public static Object[][] getOrderParams() {
    return new Object[][] {
            {
              new String[]{"BLACK"}
            },
            {
              new String[]{"BLACK", "GREY"}
            },
            {
              new String[]{}
            }
    };
  }

  @Before
  public void setUp() {
    RestAssured.baseURI = BaseHttpClient.getBASE_URL();
  }

  @Test
  @DisplayName("Create orders of /api/v1/orders")
  public void createOrders() {
    Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
    Response response = sendPostRequestAndGetResponse(order, "/api/v1/orders");
    checkStatusCode(response, 201);
    checkJsonPath(response, "track");

    Integer track = response.path("track");
    cancelOrder(new CancelOrder(track));
  }
}