package mapping_layer_tests.rows_tests;

import mapping_layer.model_rows.MovieRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRowTest {

    private UUID movieIdNo1;
    private String movieTitleNo1;
    private int movieBasePriceNo1;
    private boolean movieActiveStatusNo1;
    private UUID screeningRoomIdNo1;
    private MovieRow movieRowNo1;

    @BeforeEach
    public void init() {
        movieIdNo1 = UUID.randomUUID();
        screeningRoomIdNo1 = UUID.randomUUID();
        movieTitleNo1 = "SomeTitle";
        movieActiveStatusNo1 = true;
        movieBasePriceNo1 = 40;

        movieRowNo1 = new MovieRow(movieIdNo1, screeningRoomIdNo1, movieTitleNo1, movieActiveStatusNo1, movieBasePriceNo1);
    }

    @Test
    public void movieDocNoArgsConstructorTestPositive() {
        MovieRow movieRow = new MovieRow();
        assertNotNull(movieRow);
    }

    @Test
    public void movieDocAllArgsConstructorAndGettersTestPositive() {
        MovieRow newMovieRow = new MovieRow(movieIdNo1, screeningRoomIdNo1, movieTitleNo1, movieActiveStatusNo1, movieBasePriceNo1);
        assertNotNull(newMovieRow);
        assertEquals(movieIdNo1, newMovieRow.getMovieID());
        assertEquals(movieTitleNo1, newMovieRow.getMovieTitle());
        assertEquals(movieActiveStatusNo1, newMovieRow.isMovieStatusActive());
        assertEquals(movieBasePriceNo1, newMovieRow.getMovieBasePrice());
        assertEquals(screeningRoomIdNo1, newMovieRow.getScreeningRoomID());
    }

    @Test
    public void movieDocIdSetterTestPositive() {
        UUID idBefore = movieRowNo1.getMovieID();
        assertNotNull(idBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        movieRowNo1.setMovieID(newUUID);
        UUID idAfter = movieRowNo1.getMovieID();
        assertNotNull(idAfter);
        assertEquals(newUUID, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void movieDocTitleSetterTestPositive() {
        String titleBefore = movieRowNo1.getMovieTitle();
        assertNotNull(titleBefore);
        String newTitle = "newTitle";
        assertNotNull(newTitle);
        movieRowNo1.setMovieTitle(newTitle);
        String titleAfter = movieRowNo1.getMovieTitle();
        assertNotNull(titleAfter);
        assertEquals(newTitle, titleAfter);
        assertNotEquals(titleBefore, titleAfter);
    }

    @Test
    public void movieDocMovieActiveStatusSetterTestPositive() {
        boolean statusBefore = movieRowNo1.isMovieStatusActive();
        boolean newStatus = false;
        movieRowNo1.setMovieStatusActive(newStatus);
        boolean statusAfter = movieRowNo1.isMovieStatusActive();
        assertEquals(newStatus, statusAfter);
        assertNotEquals(statusBefore, statusAfter);
    }

    @Test
    public void movieDocMovieBasePriceSetterTestPositive() {
        double basePriceBefore = movieRowNo1.getMovieBasePrice();
        double newBasePrice = 10.0;
        movieRowNo1.setMovieBasePrice(newBasePrice);
        double basePriceAfter = movieRowNo1.getMovieBasePrice();
        assertEquals(newBasePrice, basePriceAfter);
        assertNotEquals(basePriceBefore, basePriceAfter);
    }

    @Test
    public void movieDocScreeningRoomIdSetterTestPositive() {
        UUID screeningRoomIdBefore = movieRowNo1.getScreeningRoomID();
        assertNotNull(screeningRoomIdBefore);
        UUID newScreeningRoomId = UUID.randomUUID();
        assertNotNull(newScreeningRoomId);
        movieRowNo1.setScreeningRoomID(newScreeningRoomId);
        UUID screeningRoomIdAfter = movieRowNo1.getScreeningRoomID();
        assertNotNull(screeningRoomIdAfter);
        assertEquals(newScreeningRoomId, screeningRoomIdAfter);
        assertNotEquals(screeningRoomIdBefore, screeningRoomIdAfter);
    }
}
