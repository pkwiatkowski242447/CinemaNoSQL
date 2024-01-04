package model.exceptions;

import model.Ticket;

public class TicketCreateException extends Exception {
    public TicketCreateException(String message) {
        super(message);
    }
}
