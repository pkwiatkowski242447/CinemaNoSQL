package model.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class ClientConstants {

    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier CLIENT_NAME = CqlIdentifier.fromCql("client_name");
    public static final CqlIdentifier CLIENT_SURNAME = CqlIdentifier.fromCql("client_surname");
    public static final CqlIdentifier CLIENT_AGE = CqlIdentifier.fromCql("client_age");
    public static final CqlIdentifier CLIENT_STATUS_ACTIVE = CqlIdentifier.fromCql("client_status_active");
}
