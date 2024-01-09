package model.messages;

public class TicketValidation {

    public static final String TICKET_ID_NULL = "Ticket Id could not be a reference to null.";

    public static final String MOVIE_TIME_NULL = "Time, when movie is aired, could not be null.";
    public static final String RESERVATION_TIME_NULL = "Time, when user made reservation, could not be null.";

    public static final String TICKET_BASE_PRICE_NEGATIVE = "Ticket base price could not be negative.";
    public static final String TICKET_BASE_PRICE_TOO_HIGH = "Ticket base price could not be greater than 100$.";

    public static final String MOVIE_ID_NULL = "Movie Id could not be a reference to null.";

    public static final String CLIENT_ID_NULL = "Client Id could not be a reference to null.";

    public static final String TICKET_FINAL_PRICE_NEGATIVE = "Ticket final price could not be negative.";

    public static final String TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN = "^(normal|reduced)$";
    public static final String TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE = "Given value of ticket type discriminator is incorrect.";
    public static final String TICKET_TYPE_DISCRIMINATOR_BLANK = "Ticket type discriminator could neither be blank nor null.";
}
