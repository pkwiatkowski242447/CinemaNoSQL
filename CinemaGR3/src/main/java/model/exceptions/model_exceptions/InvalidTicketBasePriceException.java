package model.exceptions.model_exceptions;

public class InvalidTicketBasePriceException extends IllegalArgumentException {
    public InvalidTicketBasePriceException(String s) {
        super(s);
    }
}
