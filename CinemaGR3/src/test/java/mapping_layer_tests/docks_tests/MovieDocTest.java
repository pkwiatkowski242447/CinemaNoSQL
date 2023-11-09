package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.MovieDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieDocTest {

    private UUID movieIdNo1;
    private String movieTitleNo1;
    private int movieBasePriceNo1;
    private boolean movieActiveStatusNo1;
    private UUID screeningRoomIdNo1;
    private MovieDoc movieDocNo1;

    @BeforeEach
    public void init() {
        movieIdNo1 = UUID.randomUUID();
        screeningRoomIdNo1 = UUID.randomUUID();
        movieTitleNo1 = "SomeTitle";
        movieActiveStatusNo1 = true;
        movieBasePriceNo1 = 40;

        movieDocNo1 = new MovieDoc(movieIdNo1, movieTitleNo1, movieActiveStatusNo1, movieBasePriceNo1, screeningRoomIdNo1);
    }

    @Test
    public void movieDocNoArgsConstructorTestPositive() {
        MovieDoc movieDoc = new MovieDoc();
        assertNotNull(movieDoc);
    }

    @Test
    public void movieDocAllArgsConstructorAndGettersTestPositive() {
        MovieDoc newMovieDoc = new MovieDoc(movieIdNo1, movieTitleNo1, movieActiveStatusNo1, movieBasePriceNo1, screeningRoomIdNo1);
        assertNotNull(newMovieDoc);
        assertEquals(movieIdNo1, newMovieDoc.getMovieID());
        assertEquals(movieTitleNo1, newMovieDoc.getMovieTitle());
        assertEquals(movieActiveStatusNo1, newMovieDoc.isMovieStatusActive());
        assertEquals(movieBasePriceNo1, newMovieDoc.getMovieBasePrice());
        assertEquals(screeningRoomIdNo1, newMovieDoc.getScreeningRoomID());
    }

    @Test
    public void movieDocIdSetterTestPositive() {
        UUID idBefore = movieDocNo1.getMovieID();
        assertNotNull(idBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        movieDocNo1.setMovieID(newUUID);
        UUID idAfter = movieDocNo1.getMovieID();
        assertNotNull(idAfter);
        assertEquals(newUUID, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void movieDocTitleSetterTestPositive() {
        String titleBefore = movieDocNo1.getMovieTitle();
        assertNotNull(titleBefore);
        String newTitle = "newTitle";
        assertNotNull(newTitle);
        movieDocNo1.setMovieTitle(newTitle);
        String titleAfter = movieDocNo1.getMovieTitle();
        assertNotNull(titleAfter);
        assertEquals(newTitle, titleAfter);
        assertNotEquals(titleBefore, titleAfter);
    }

    @Test
    public void movieDocMovieActiveStatusSetterTestPositive() {
        boolean statusBefore = movieDocNo1.isMovieStatusActive();
        boolean newStatus = false;
        movieDocNo1.setMovieStatusActive(newStatus);
        boolean statusAfter = movieDocNo1.isMovieStatusActive();
        assertEquals(newStatus, statusAfter);
        assertNotEquals(statusBefore, statusAfter);
    }

    @Test
    public void movieDocMovieBasePriceSetterTestPositive() {
        double basePriceBefore = movieDocNo1.getMovieBasePrice();
        double newBasePrice = 10.0;
        movieDocNo1.setMovieBasePrice(newBasePrice);
        double basePriceAfter = movieDocNo1.getMovieBasePrice();
        assertEquals(newBasePrice, basePriceAfter);
        assertNotEquals(basePriceBefore, basePriceAfter);
    }

    @Test
    public void movieDocScreeningRoomIdSetterTestPositive() {
        UUID screeningRoomIdBefore = movieDocNo1.getScreeningRoomID();
        assertNotNull(screeningRoomIdBefore);
        UUID newScreeningRoomId = UUID.randomUUID();
        assertNotNull(newScreeningRoomId);
        movieDocNo1.setScreeningRoomID(newScreeningRoomId);
        UUID screeningRoomIdAfter = movieDocNo1.getScreeningRoomID();
        assertNotNull(screeningRoomIdAfter);
        assertEquals(newScreeningRoomId, screeningRoomIdAfter);
        assertNotEquals(screeningRoomIdBefore, screeningRoomIdAfter);
    }
}
