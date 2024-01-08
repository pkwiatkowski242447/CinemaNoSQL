package model.messages;

public class MovieValidation {

    public static final String MOVIE_ID_NULL = "Movie Id could not be a reference to null.";
    public static final String MOVIE_TITLE_BLANK = "Movie title could neither be blank nor null.";

    public static final String MOVIE_TITLE_TOO_SHORT = "Movie title must be at least 1 character long.";
    public static final String MOVIE_TITLE_TOO_LONG = "Movie title could not be longer than 150 characters long.";

    public static final String MOVIE_BASE_PRICE_NEGATIVE = "Movie base price could not be negative.";
    public static final String MOVIE_BASE_PRICE_TOO_HIGH = "Movie base price could not exceed 100$.";

    public static final String NUMBER_OF_AVAILABLE_SEATS_NEGATIVE = "Number of available seats could not be negative.";
    public static final String NUMBER_OF_AVAILABLE_SEATS_TOO_HIGH = "Number of available seats could not higher than 150.";

    public static final String SCREENING_ROOM_NUMBER_TOO_LOW = "Number of the screening room must be at least 1.";
    public static final String SCREENING_ROOM_NUMBER_TOO_HIGH = "Number of the screening room could not exceed 20.";
}
