package model.messages;

import model.constants.TicketConstants;

public class TicketValidation {

    public static final String UUID_REGEX_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String TICKET_ID_NULL = "Ticket Id could not be a reference to null.";
    public static final String TICKET_ID_NOT_UUID = "Given ticket Id is not UUID.";

    public static final String MOVIE_TIME_NULL = "Time, when movie is aired, could not be null.";
    public static final String RESERVATION_TIME_NULL = "Time, when user made reservation, could not be null.";

    public static final String TICKET_BASE_PRICE_NEGATIVE = "Ticket base price could not be negative.";
    public static final String TICKET_BASE_PRICE_TOO_HIGH = "Ticket base price could not be greater than 100$.";

    public static final String MOVIE_ID_NOT_UUID = "Given movie Id is not UUID.";
    public static final String MOVIE_ID_NULL = "Movie Id could not be a reference to null.";

    public static final String CLIENT_ID_NOT_UUID = "Given client Id is not UUID.";
    public static final String CLIENT_ID_NULL = "Client Id could not be a reference to null.";

    public static final String TICKET_FINAL_PRICE_NEGATIVE = "Ticket final price could not be negative.";

    public static final String TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN = "/^(" + TicketConstants.NORMAL_TICKET + "|" + TicketConstants.REDUCED_TICKET + ")$/";
    public static final String TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE = "Given value of ticket type discriminator is incorrect.";
    public static final String TICKET_TYPE_DISCRIMINATOR_BLANK = "Ticket type discriminator could neither be blank nor null.";
}
