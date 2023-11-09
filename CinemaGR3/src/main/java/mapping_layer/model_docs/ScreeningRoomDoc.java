package mapping_layer.model_docs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class ScreeningRoomDoc {

    @BsonProperty("_id")
    private UUID screeningRoomID;

    @BsonProperty("screening_room_floor")
    private int screeningRoomFloor;

    @BsonProperty("screening_room_number")
    private int screeningRoomNumber;

    @BsonProperty("number_of_available_seats")
    private int numberOfAvailableSeats;

    @BsonProperty("screening_room_status_active")
    private boolean screeningRoomStatusActive;

    // Constructor

    @BsonCreator
    public ScreeningRoomDoc(@BsonProperty("_id") UUID screeningRoomID,
                            @BsonProperty("screening_room_floor") int screeningRoomFloor,
                            @BsonProperty("screening_room_number") int screeningRoomNumber,
                            @BsonProperty("number_of_available_seats") int numberOfAvailableSeats,
                            @BsonProperty("screening_room_status_active") boolean screeningRoomStatusActive) {
        this.screeningRoomID = screeningRoomID;
        this.screeningRoomFloor = screeningRoomFloor;
        this.screeningRoomNumber = screeningRoomNumber;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.screeningRoomStatusActive = screeningRoomStatusActive;
    }
}
