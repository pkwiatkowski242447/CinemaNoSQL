package model.dtos.converter;

import model.constants.TicketConstants;
import model.dtos.TicketClientDTO;
import model.dtos.TicketMovieDTO;
import model.exceptions.TicketTypeNotFoundException;
import model.model.Ticket;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;

public class TicketConverter {

    public static TicketClientDTO toTicketClientDTO(Ticket ticket) {
        return new TicketClientDTO(ticket.getClientId(),
                ticket.getTicketID(),
                ticket.getMovieTime(),
                ticket.getReservationTime(),
                ticket.getTicketBasePrice(),
                ticket.getTicketFinalPrice(),
                ticket.getMovieId(),
                ticket.getTicketTypeDiscriminator()
        );
    }

    public static TicketMovieDTO toTicketMovieDTO(Ticket ticket) {
        return new TicketMovieDTO(ticket.getMovieId(),
                ticket.getTicketID(),
                ticket.getMovieTime(),
                ticket.getReservationTime(),
                ticket.getTicketBasePrice(),
                ticket.getTicketFinalPrice(),
                ticket.getClientId(),
                ticket.getTicketTypeDiscriminator()
        );
    }

    public static Ticket toTicketFromTicketClientDTO(TicketClientDTO ticketClientDTO) throws TicketTypeNotFoundException {
        return switch (ticketClientDTO.getTicketTypeDiscriminator()) {
            case TicketConstants.REDUCED_TICKET -> new Reduced(
                    ticketClientDTO.getTicketID(),
                    ticketClientDTO.getMovieTime(),
                    ticketClientDTO.getReservationTime(),
                    ticketClientDTO.getTicketBasePrice(),
                    ticketClientDTO.getTicketBasePrice(),
                    ticketClientDTO.getMovieId(),
                    ticketClientDTO.getClientId(),
                    ticketClientDTO.getTicketTypeDiscriminator()
            );
            case TicketConstants.NORMAL_TICKET -> new Normal(
                    ticketClientDTO.getTicketID(),
                    ticketClientDTO.getMovieTime(),
                    ticketClientDTO.getReservationTime(),
                    ticketClientDTO.getTicketBasePrice(),
                    ticketClientDTO.getTicketFinalPrice(),
                    ticketClientDTO.getMovieId(),
                    ticketClientDTO.getClientId(),
                    ticketClientDTO.getTicketTypeDiscriminator()
            );
            default -> throw new TicketTypeNotFoundException("Could not create ticket of given type.");
        };
    }

    public static Ticket toTicketFromTicketMovieDTO(TicketMovieDTO ticketMovieDTO) throws TicketTypeNotFoundException {
        return switch (ticketMovieDTO.getTicketTypeDiscriminator()) {
            case TicketConstants.REDUCED_TICKET -> new Reduced(
                    ticketMovieDTO.getTicketID(),
                    ticketMovieDTO.getMovieTime(),
                    ticketMovieDTO.getReservationTime(),
                    ticketMovieDTO.getTicketBasePrice(),
                    ticketMovieDTO.getMovieId(),
                    ticketMovieDTO.getClientId()
            );
            case TicketConstants.NORMAL_TICKET -> new Normal(
                    ticketMovieDTO.getTicketID(),
                    ticketMovieDTO.getMovieTime(),
                    ticketMovieDTO.getReservationTime(),
                    ticketMovieDTO.getTicketBasePrice(),
                    ticketMovieDTO.getMovieId(),
                    ticketMovieDTO.getClientId()
            );
            default -> throw new TicketTypeNotFoundException("Could not create ticket of given type.");
        };
    }
}
