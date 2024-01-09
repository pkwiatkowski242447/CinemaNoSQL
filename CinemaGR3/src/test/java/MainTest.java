import main.Main;
import model.exceptions.MongoConfigNotFoundException;
import org.junit.jupiter.api.Test;

public class MainTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void mainConstructorTest() {
        Main main = new Main();
    }

    @Test
    public void mainTestRun() throws MongoConfigNotFoundException {
        Main.main(null);
    }
}
