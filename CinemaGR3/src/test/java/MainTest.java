import main.Main;
import model.exceptions.CassandraConfigNotFound;
import org.junit.jupiter.api.Test;

public class MainTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void mainConstructorTest() {
        Main main = new Main();
    }

    @Test
    public void mainTestRun() throws CassandraConfigNotFound {
        Main.main(null);
    }
}
