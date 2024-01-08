package model.messages;

public class ClientValidation {

    public static final String CLIENT_ID_NAME = "Client id could not be a reference to null.";
    public static final String CLIENT_NAME_BLANK = "Client name could neither be blank nor null.";
    public static final String CLIENT_SURNAME_BLANK = "Client name could neither be blank nor null";

    public static final String CLIENT_NAME_TOO_SHORT = "Client name must be at least 1 character long.";
    public static final String CLIENT_NAME_TOO_LONG = "Client name could not be longer than 50 characters.";

    public static final String CLIENT_SURNAME_TOO_SHORT = "Client surname must be at least 2 character long.";
    public static final String CLIENT_SURNAME_TOO_LONG = "Client surname could not be longer than 100 characters.";

    public static final String CLIENT_AGE_TOO_LOW = "Client age must be at least 18 years old.";
    public static final String CLIENT_AGE_TOO_HIGH = "Client age could not be higher than 120 years.";
}
