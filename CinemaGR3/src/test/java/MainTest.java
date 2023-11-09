import main.Main;
import org.junit.jupiter.api.Test;

public class MainTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void mainConstructorTest() {
        Main main = new Main();
    }

    @Test
    public void mainTestRun() {
        Main.main(null);
    }
}
