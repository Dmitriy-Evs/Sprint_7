import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.BaseUrl;
import org.example.Courier;
import org.example.StepCourier;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.client.methods.RequestBuilder.delete;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    static  private String firstName =RandomStringUtils.randomAlphabetic(6);
    static private String password = RandomStringUtils.randomAlphabetic(4);
    static private String login = RandomStringUtils.randomAlphabetic(8);
    static private String PEN_CREATE = "/api/v1/courier";
    static private String PEN_LOGIN = "/api/v1/courier/login";
    static private String PEN_DELETE = "/api/v1/courier/";

    // Метод для логирования запросов и ответов
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.baseUrl;
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
        Response response = StepCourier.sendGetRequestCourier();
        response.then().assertThat().statusCode(SC_CREATED).and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание нового курьера без обязательного поля login")
    @Description("/api/v1/courier post: password, firstName")
    public  void createCourierWithoutFirstName() {
        Response response = StepCourier.sendGetRequestCourierWithoutFirstname();
        response.then().assertThat().statusCode(SC_BAD_REQUEST).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("/api/v1/courier post: login, password, firstName")
    public void createSameCourier(){
        Response response = StepCourier.sendGetRequestCourier();
        response.then().assertThat().statusCode(SC_CONFLICT).and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }


}



