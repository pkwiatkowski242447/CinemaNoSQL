package mapping_layer.mappers;

import mapping_layer.model_docs.ClientDoc;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import mapping_layer.model_docs.TicketDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.Ticket;
import model.exceptions.model_docs_exceptions.*;

public class TicketMapper {

    public static TicketDoc toTicketDoc(Ticket ticket) {
        TicketDoc ticketDoc = new TicketDoc();
        ticketDoc.setTicketID(ticket.getTicketID());
        if (ticket.getMovieTime() != null) {
            ticketDoc.setMovieTime(ticket.getMovieTime());
        } else {
            throw new NullMovieTimeException("Reference to movie time is null.");
        }
        if (ticket.getReservationTime() != null) {
            ticketDoc.setReservationTime(ticket.getReservationTime());
        } else {
            throw new NullReservationTimeException("Reference to reservation time is null.");
        }
        ticketDoc.setTicketFinalPrice(ticket.getTicketFinalPrice());
        ticketDoc.setTicketStatusActive(ticket.isTicketStatusActive());
        ticketDoc.setMovieID(ticket.getMovie().getMovieID());
        if (ticket.getClient() != null) {
            ticketDoc.setClientID(ticket.getClient().getClientID());
        } else {
            throw new ClientNullException("Reference to client object is null.");
        }
        ticketDoc.setTypeOfTicketID(ticket.getTicketType().getTicketTypeID());
        return ticketDoc;
    }

    public static Ticket toTicket(TicketDoc ticketDoc,
                                  MovieDoc movieDoc,
                                  ScreeningRoomDoc screeningRoomDoc,
                                  ClientDoc clientDoc,
                                  TypeOfTicketDoc typeOfTicketDoc,
                                  String clazz) {
        return new Ticket(ticketDoc.getTicketID(),
                ticketDoc.getMovieTime(),
                ticketDoc.getReservationTime(),
                ticketDoc.isTicketStatusActive(),
                ticketDoc.getTicketFinalPrice(),
                MovieMapper.toMovie(movieDoc, screeningRoomDoc),
                ClientMapper.toClient(clientDoc),
                TypeOfTicketMapper.toTypeOfTicket(typeOfTicketDoc, clazz));
    }
}
