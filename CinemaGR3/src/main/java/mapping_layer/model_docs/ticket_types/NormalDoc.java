package mapping_layer.model_docs.ticket_types;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "normal")
public class NormalDoc extends TypeOfTicketDoc {

    @BsonCreator
    public NormalDoc(@BsonProperty("_id") UUID typeOfTicketID) {
        super(typeOfTicketID);
    }

    @Override
    public UUID getTypeOfTicketID() {
        return super.getTypeOfTicketID();
    }
}
