package mapping_layer.model_docs;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@Entity(defaultKeyspace = "cinema")
@CqlName("screening_rooms")
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class ScreeningRoomRow {

    @PartitionKey
    @CqlName("screening_room_id")
    private UUID screeningRoomID;

    @CqlName("screening_room_floor")
    private int screeningRoomFloor;

    @CqlName("screening_room_number")
    private int screeningRoomNumber;

    @CqlName("number_of_available_seats")
    private int numberOfAvailableSeats;

    @CqlName("screening_room_status_active")
    private boolean screeningRoomStatusActive;

    // Constructor

    public ScreeningRoomRow(UUID screeningRoomID,
                            int screeningRoomFloor,
                            int screeningRoomNumber,
                            int numberOfAvailableSeats,
                            boolean screeningRoomStatusActive) {
        this.screeningRoomID = screeningRoomID;
        this.screeningRoomFloor = screeningRoomFloor;
        this.screeningRoomNumber = screeningRoomNumber;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.screeningRoomStatusActive = screeningRoomStatusActive;
    }
}
