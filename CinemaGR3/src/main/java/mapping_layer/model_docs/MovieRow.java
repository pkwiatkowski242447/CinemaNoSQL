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

@Getter @Setter
@NoArgsConstructor
@Entity(defaultKeyspace = "cinema")
@CqlName("movies")
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class MovieRow {

    @CqlName("movie_id")
    private UUID movieID;

    @CqlName("movie_title")
    private String movieTitle;

    @CqlName("movie_status_active")
    private boolean movieStatusActive;

    @CqlName("movie_base_price")
    private double movieBasePrice;

    @CqlName("screening_room_id")
    private UUID screeningRoomID;

    // Constructor

    public MovieRow(UUID movieID,
                    String movieTitle,
                    boolean movieStatusActive,
                    double movieBasePrice,
                    UUID screeningRoomID) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieStatusActive = movieStatusActive;
        this.movieBasePrice = movieBasePrice;
        this.screeningRoomID = screeningRoomID;
    }
}
