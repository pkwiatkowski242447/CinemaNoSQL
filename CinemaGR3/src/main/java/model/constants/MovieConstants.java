package model.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class MovieConstants {

    public static final CqlIdentifier MOVIE_ID = CqlIdentifier.fromCql("movie_id");
    public static final CqlIdentifier MOVIE_TITLE = CqlIdentifier.fromCql("movie_title");
    public static final CqlIdentifier MOVIE_STATUS_ACTIVE = CqlIdentifier.fromCql("movie_status_active");
    public static final CqlIdentifier MOVIE_BASE_PRICE = CqlIdentifier.fromCql("movie_base_price");
    public static final CqlIdentifier SCREENING_ROOM_ID = CqlIdentifier.fromCql("screening_room_id");
}
