package model.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class ScreeningRoomConstants {

    public static final CqlIdentifier SCREENING_ROOM_ID = CqlIdentifier.fromCql("screening_room_id");
    public static final CqlIdentifier SCREENING_ROOM_FLOOR = CqlIdentifier.fromCql("screening_room_floor");
    public static final CqlIdentifier SCREENING_ROOM_NUMBER = CqlIdentifier.fromCql("screening_room_number");
    public static final CqlIdentifier NUMBER_OF_AVAILABLE_SEATS = CqlIdentifier.fromCql("number_of_available_seats");
    public static final CqlIdentifier SCREENING_ROOM_STATUS_ACTIVE = CqlIdentifier.fromCql("screening_room_status_active");
}
