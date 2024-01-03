package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import mapping_layer.model_rows.TicketRow;
import model.Client;
import model.Movie;
import model.Ticket;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TicketQueryProvider {

    private final CqlSession session;
    private final EntityHelper<TicketRow> ticketEntityHelper;

    public TicketQueryProvider(MapperContext mapperContext, EntityHelper<TicketRow> ticketEntityHelper) {
        this.session = mapperContext.getSession();
        this.ticketEntityHelper = ticketEntityHelper;
    }

    // Create methods

    public Ticket create(Instant movieTime, Instant reservationTime, Movie movie, Client client, String typeOfTicket) {
        return null;
    }

    // Read methods

    public Ticket findByUUID(UUID ticketId) {
        return null;
    }

    public List<Ticket> findAll() {
        return null;
    }

    public List<Ticket> findAllActive() {
        return null;
    }

    public List<UUID> findAllUUIDs() {
        return null;
    }

    // Update methods

    public void update(Ticket ticket) {

    }

    public void expire(Ticket ticket) {

    }

    // Delete methods

    public void delete(Ticket ticket) {

    }

    public void deleteByUUID(UUID ticketId) {

    }
}
