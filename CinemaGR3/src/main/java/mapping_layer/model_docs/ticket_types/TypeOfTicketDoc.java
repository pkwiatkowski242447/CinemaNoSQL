package mapping_layer.model_docs.ticket_types;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz")
public abstract class TypeOfTicketDoc {

    @BsonProperty("_id")
    private UUID typeOfTicketID;

    @BsonCreator
    public TypeOfTicketDoc(@BsonProperty("_id") UUID typeOfTicketID) {
        this.typeOfTicketID = typeOfTicketID;
    }

    // Getters

    public UUID getTypeOfTicketID() {
        return typeOfTicketID;
    }
}
