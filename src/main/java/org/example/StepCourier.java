package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class StepCourier {
    static  private String firstName = RandomStringUtils.randomAlphabetic(6);
    static private String password = RandomStringUtils.randomAlphabetic(4);
    static private String login = RandomStringUtils.randomAlphabetic(8);
    static private String PEN_CREATE = "/api/v1/courier";
    @Step("Отправить запрос to /api/v1/courier")
    public static Response sendGetRequestCourierWithoutFirstname() {
        Courier courierCreate = new Courier("", password, firstName);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(PEN_CREATE);
    }

    @Step("Отправить запрос to /api/v1/courier")
    public static Response sendGetRequestCourier() {
        Courier courierCreate = new Courier(login, password, firstName);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(PEN_CREATE);
    }
}
