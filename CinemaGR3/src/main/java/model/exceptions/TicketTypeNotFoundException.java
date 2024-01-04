package model.exceptions;

public class TicketTypeNotFoundException extends ObjectNotFoundException {
    public TicketTypeNotFoundException(String msg) {
        super(msg);
    }
}
