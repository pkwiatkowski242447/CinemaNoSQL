package mapping_layer.model_docs;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.NamingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(defaultKeyspace = "cinema")
@CqlName("clients")
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class ClientRow {

    @CqlName("client_id")
    private UUID clientID;

    @CqlName("client_name")
    private String clientName;

    @CqlName("client_surname")
    private String clientSurname;

    @CqlName("client_age")
    private int clientAge;

    @CqlName("client_status_active")
    private boolean clientStatusActive;

    // Constructor

    public ClientRow(UUID clientID,
                     String clientName,
                     String clientSurname,
                     int clientAge,
                     boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }
}
