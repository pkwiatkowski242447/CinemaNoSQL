package mapping_layer.model_docs;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class MovieDoc {

    @BsonProperty("_id")
    @JsonbProperty("id")
    private UUID movieID;

    @BsonProperty("movie_title")
    @JsonbProperty("movie_title")
    private String movieTitle;

    @BsonProperty("movie_status_active")
    @JsonbProperty("movie_status_active")
    private boolean movieStatusActive;

    @BsonProperty("movie_base_price")
    @JsonbProperty("movie_base_price")
    private double movieBasePrice;

    @BsonProperty("screening_room_ref")
    @JsonbProperty("screening_room_ref")
    private UUID screeningRoomID;

    // Constructor

    @BsonCreator
    @JsonbCreator
    public MovieDoc(@BsonProperty("_id") @JsonbProperty("id") UUID movieID,
                    @BsonProperty("movie_title") @JsonbProperty("movie_title") String movieTitle,
                    @BsonProperty("movie_status_active") @JsonbProperty("movie_status_active") boolean movieStatusActive,
                    @BsonProperty("movie_base_price") @JsonbProperty("movie_base_price") double movieBasePrice,
                    @BsonProperty("screening_room_ref") @JsonbProperty("screening_room_ref") UUID screeningRoomID) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieStatusActive = movieStatusActive;
        this.movieBasePrice = movieBasePrice;
        this.screeningRoomID = screeningRoomID;
    }
}
