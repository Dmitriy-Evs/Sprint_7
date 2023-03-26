import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Order;
import org.example.OrderAsserts;
import org.example.StepOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private StepOrder step;
    private OrderAsserts check;
    private String[] color;
    int track;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void preconditions() {
        step = new StepOrder();
        check= new OrderAsserts();
    }

    @Parameterized.Parameters(name = "scooter's color")
    public static Object[][] valueColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{}}
        };
    }
    @Test
    @Step("Create order with different colors")
    public void createTest() {
        Order order = new Order(color);
        ValidatableResponse response = step.createOrder(order);
        check.checkBodyOrder(response);
        track = response.extract().path("track");
    }
    @After
    public void deleteOrder(){
        step.cancelOrder(track);
    }
}