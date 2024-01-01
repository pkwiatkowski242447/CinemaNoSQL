package mapping_layer.converters;

import mapping_layer.model_docs.ClientRow;
import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import mapping_layer.model_docs.TicketRow;
import mapping_layer.model_docs.ticket_types.TypeOfTicketRow;
import model.Ticket;
import model.exceptions.model_docs_exceptions.TicketCreateException;
import model.exceptions.model_docs_exceptions.date_null_exceptions.NullMovieTimeException;
import model.exceptions.model_docs_exceptions.date_null_exceptions.NullReservationTimeException;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ClientNullException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.MovieNullException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ObjectNullReferenceException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.TypeOfTicketNullException;

public class TicketConverter {

    public static TicketRow toTicketRow(Ticket ticket) throws ObjectNullReferenceException {
        TicketRow ticketRow = new TicketRow();
        ticketRow.setTicketID(ticket.getTicketID());

        if (ticket.getMovieTime() != null) {
            ticketRow.setMovieTime(ticket.getMovieTime());
        } else {
            throw new NullMovieTimeException("Reference to movie time is null.");
        }
        if (ticket.getReservationTime() != null) {
            ticketRow.setReservationTime(ticket.getReservationTime());
        } else {
            throw new NullReservationTimeException("Reference to reservation time is null.");
        }

        ticketRow.setTicketFinalPrice(ticket.getTicketFinalPrice());
        ticketRow.setTicketStatusActive(ticket.isTicketStatusActive());
        ticketRow.setMovieID(ticket.getMovie().getMovieID());

        if (ticket.getMovie() != null) {
            ticketRow.setMovieID(ticket.getMovie().getMovieID());
        } else {
            throw new MovieNullException("Reference to movie object is null.");
        }
        if (ticket.getClient() != null) {
            ticketRow.setClientID(ticket.getClient().getClientID());
        } else {
            throw new ClientNullException("Reference to client object is null.");
        }
        if (ticket.getTicketType() != null) {
            ticketRow.setTypeOfTicketID(ticket.getTicketType().getTicketTypeID());
        } else {
            throw new TypeOfTicketNullException("Reference to ticket type is null.");
        }

        return ticketRow;
    }

    public static Ticket toTicket(TicketRow ticketRow,
                                  MovieRow movieRow,
                                  ScreeningRoomRow screeningRoomRow,
                                  ClientRow clientRow,
                                  TypeOfTicketRow typeOfTicketRow) throws TicketCreateException {
        try {
            return new Ticket(ticketRow.getTicketID(),
                    ticketRow.getMovieTime(),
                    ticketRow.getReservationTime(),
                    ticketRow.isTicketStatusActive(),
                    ticketRow.getTicketFinalPrice(),
                    MovieConverter.toMovie(movieRow, screeningRoomRow),
                    ClientConverter.toClient(clientRow),
                    TypeOfTicketConverter.toTypeOfTicket(typeOfTicketRow));
        } catch (TypeOfTicketNotFoundException exception) {
            throw new TicketCreateException("Ticket object could not be created for given ticket row object");
        }
    }
}
