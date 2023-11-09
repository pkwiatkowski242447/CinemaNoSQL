package mapping_layer.model_docs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientDoc {

    @BsonProperty("_id")
    private UUID clientID;

    @BsonProperty("client_name")
    private String clientName;

    @BsonProperty("client_surname")
    private String clientSurname;

    @BsonProperty("client_age")
    private int clientAge;

    @BsonProperty("client_status_active")
    private boolean clientStatusActive;

    // Constructor

    @BsonCreator
    public ClientDoc(@BsonProperty("_id") UUID clientID,
                     @BsonProperty("client_name") String clientName,
                     @BsonProperty("client_surname") String clientSurname,
                     @BsonProperty("client_age") int clientAge,
                     @BsonProperty("client_status_active") boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }
}
