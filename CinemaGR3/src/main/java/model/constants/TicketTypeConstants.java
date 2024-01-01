package model.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class TicketTypeConstants {

    public static final CqlIdentifier TICKET_TYPE_ID = CqlIdentifier.fromCql("ticket_type_id");
    public static final CqlIdentifier TICKET_TYPE_DISCRIMINATOR = CqlIdentifier.fromCql("ticket_type_discriminator");

    public static final String NORMAL_TICKET = "normal";
    public static final String REDUCED_TICKET = "reduced";
}
