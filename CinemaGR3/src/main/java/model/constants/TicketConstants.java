package model.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class TicketConstants {

    public static final CqlIdentifier TICKET_ID = CqlIdentifier.fromCql("ticket_id");
    public static final CqlIdentifier MOVIE_TIME = CqlIdentifier.fromCql("movie_time");
    public static final CqlIdentifier RESERVATION_TIME = CqlIdentifier.fromCql("reservation_time");
    public static final CqlIdentifier TICKET_STATUS_ACTIVE = CqlIdentifier.fromCql("ticket_status_active");
    public static final CqlIdentifier TICKET_FINAL_PRICE = CqlIdentifier.fromCql("ticket_final_price");
    public static final CqlIdentifier MOVIE_ID = CqlIdentifier.fromCql("movie_id");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier TYPE_OF_TICKET_ID = CqlIdentifier.fromCql("type_of_ticket_id");
}
