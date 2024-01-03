package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import mapping_layer.converters.TypeOfTicketConverter;
import mapping_layer.model_rows.ticket_types.NormalRow;
import mapping_layer.model_rows.ticket_types.ReducedRow;
import mapping_layer.model_rows.ticket_types.TypeOfTicketRow;
import model.constants.TicketTypeConstants;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.exceptions.repository_exceptions.create_exceptions.TypeOfTicketRepositoryCreateException;
import model.exceptions.repository_exceptions.delete_exceptions.TypeOfTicketRepositoryDeleteException;
import model.exceptions.repository_exceptions.read_exceptions.TypeOfTicketRepositoryReadException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;

import java.util.Objects;
import java.util.UUID;

public class TypeOfTicketQueryProvider {

    private final CqlSession session;
    private final EntityHelper<NormalRow> normalEntityHelper;
    private final EntityHelper<ReducedRow> reducedEntityHelper;

    public TypeOfTicketQueryProvider(MapperContext mapperContext, EntityHelper<NormalRow> normalEntityHelper, EntityHelper<ReducedRow> reducedEntityHelper) {
        this.session = mapperContext.getSession();
        this.normalEntityHelper = normalEntityHelper;
        this.reducedEntityHelper = reducedEntityHelper;
    }

    // Create methods

    public TypeOfTicket createNormal() throws TypeOfTicketRepositoryCreateException {
        Normal ticketTypeNormal = new Normal();

        TypeOfTicketRow normalModelRow = TypeOfTicketConverter.toTypeOfTicketRow(ticketTypeNormal);

        BoundStatement createNormal = session.prepare(normalEntityHelper.insert().build())
                .bind()
                .setUuid(TicketTypeConstants.TICKET_TYPE_ID, normalModelRow.getTypeOfTicketID())
                .setString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR, normalModelRow.getTicketTypeDiscriminator());
        Row normalRow = session.execute(createNormal).one();

        if (normalRow != null) {
            try {
                return this.convertTypeOfTicketRowToTypeOfTicket(normalRow);
            } catch (TypeOfTicketNotFoundException exception) {
                throw new TypeOfTicketRepositoryCreateException("Normal ticket type could not be created in the database since ticket type is invalid.");
            }
        } else {
            throw new TypeOfTicketRepositoryCreateException("Normal ticket type could not be created in the database.");
        }
    }

    public TypeOfTicket createReduced() throws TypeOfTicketRepositoryCreateException {
        Reduced ticketTypeNormal = new Reduced();

        TypeOfTicketRow reducedModelRow = TypeOfTicketConverter.toTypeOfTicketRow(ticketTypeNormal);

        BoundStatement createReduced = session.prepare(reducedEntityHelper.insert().build())
                .bind()
                .setUuid(TicketTypeConstants.TICKET_TYPE_ID, reducedModelRow.getTypeOfTicketID())
                .setString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR, reducedModelRow.getTicketTypeDiscriminator());
        Row reducedRow = session.execute(createReduced).one();

        if (reducedRow != null) {
            try {
                return this.convertTypeOfTicketRowToTypeOfTicket(reducedRow);
            } catch (TypeOfTicketNotFoundException exception) {
                throw new TypeOfTicketRepositoryCreateException("Reduced ticket type could not be created in the database since ticket type is invalid.");
            }
        } else {
            throw new TypeOfTicketRepositoryCreateException("Reduced ticket type could not be created in the database.");
        }
    }

    // Read methods

    public TypeOfTicket findByUUID(UUID typeOfTicketId) throws TypeOfTicketRepositoryReadException {
        Select findById = QueryBuilder
                .selectFrom(TicketTypeConstants.TICKET_TYPES_TABLE_NAME)
                .all()
                .where(Relation.column(TicketTypeConstants.TICKET_TYPE_ID).isEqualTo(QueryBuilder.literal(typeOfTicketId)));
        Row typeOfTicketRow = session.execute(findById.build()).one();

        if (typeOfTicketRow != null) {
            try {
                return this.convertTypeOfTicketRowToTypeOfTicket(typeOfTicketRow);
            } catch (TypeOfTicketNotFoundException exception) {
                throw new TypeOfTicketRepositoryReadException("Type of ticket to be read consists of incorrect ticket type.");
            }
        } else {
            throw new TypeOfTicketRepositoryReadException("Type of ticket with given Id could not be found in the database.");
        }
    }

    // Delete methods

    public void delete(TypeOfTicket typeOfTicket) throws TypeOfTicketRepositoryDeleteException {
        this.deleteByUUID(typeOfTicket.getTicketTypeID());
    }

    public void deleteByUUID(UUID typeOfTicketId) throws TypeOfTicketRepositoryDeleteException {
        Delete deleteById = QueryBuilder
                .deleteFrom(TicketTypeConstants.TICKET_TYPES_TABLE_NAME)
                .where(Relation.column(TicketTypeConstants.TICKET_TYPE_ID).isEqualTo(QueryBuilder.literal(typeOfTicketId)));
        Row typeOfTicketRow = session.execute(deleteById.build()).one();

        if (typeOfTicketRow == null) {
            throw new TypeOfTicketRepositoryDeleteException("Type of ticket with given Id could not be deleted from the database.");
        }
    }

    // Additional methods

    private TypeOfTicket convertTypeOfTicketRowToTypeOfTicket(Row typeOfTicketRow) throws TypeOfTicketNotFoundException {
        TypeOfTicketRow typeOfTicketModelRow;
        if (Objects.equals(typeOfTicketRow.getString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR), TicketTypeConstants.REDUCED_TICKET)) {
            typeOfTicketModelRow = new NormalRow(
                    typeOfTicketRow.getUuid(TicketTypeConstants.TICKET_TYPE_ID),
                    typeOfTicketRow.getString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR)
            );
        } else if (Objects.equals(typeOfTicketRow.getString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR), TicketTypeConstants.NORMAL_TICKET)) {
            typeOfTicketModelRow = new ReducedRow(
                    typeOfTicketRow.getUuid(TicketTypeConstants.TICKET_TYPE_ID),
                    typeOfTicketRow.getString(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR)
            );
        } else {
            throw new TypeOfTicketNotFoundException("Invalid type of ticket.");
        }

        return TypeOfTicketConverter.toTypeOfTicket(typeOfTicketModelRow);
    }
}
