import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.client.methods.RequestBuilder.delete;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    static  private String firstName ="Dima";
    static private String password ="11112222333";
    static private String login = RandomStringUtils.randomAlphabetic(8);
    static private String PEN_CREATE = "/api/v1/courier";
    static private String PEN_LOGIN = "/api/v1/courier/login";
    static private String PEN_DELETE = "/api/v1/courier/";
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown (){
        Courier courierDelete = new Courier(login, password,firstName );
        String response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierDelete)
                .when()
                .post(PEN_LOGIN)
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        String userId = jsonPath.getString("id");
        delete(PEN_DELETE + userId);
    }

    @Test
    @DisplayName("Создание нового курьера с правильными данными")
    public void createNewCourierAndCheckResponse(){
        Courier courierCreate  = new Courier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(PEN_CREATE)
                .then().assertThat().statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Создание нового курьера без обязательного поля firstName")
    @Description("/api/v1/courier post: login, password")
    public  void createCourierWithoutFirstName() {
        Courier courierCreate  = new Courier(login,password);
        given()
                .body(courierCreate)
                .when()
                .post(PEN_CREATE)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("/api/v1/courier post: login, password, firstName")
    public void createSameCourier(){
        Courier courierCreate  = new Courier(login, password, firstName);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(PEN_CREATE)
                .then().assertThat().statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}