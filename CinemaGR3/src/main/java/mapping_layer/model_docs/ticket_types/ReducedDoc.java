package mapping_layer.model_docs.ticket_types;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@BsonDiscriminator(key = "_clazz", value = "reduced")
public class ReducedDoc extends TypeOfTicketDoc {

    @BsonCreator
    public ReducedDoc(@BsonProperty("_id") UUID typeOfTicketID) {
        super(typeOfTicketID);
    }

    @Override
    public UUID getTypeOfTicketID() {
        return super.getTypeOfTicketID();
    }
}
