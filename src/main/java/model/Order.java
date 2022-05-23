package model;

public class Order {
  public String firstName;
  public String lastName;
  public String address;
  public Integer metroStation;
  public String phone;
  public Integer rentTime;
  public String deliveryDate;
  public String comment;
  public String[] color;

  public Order() {}

  public Order(String firstName, String lastName, String address, Integer metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
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
