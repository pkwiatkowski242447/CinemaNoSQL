package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name = "movie_id", nullable = false, unique = true)
    private UUID movieID;

    @Column(name = "movie_title", nullable = false)
    @Length(min = 1, max = 150)
    private String movieTitle;

    @Column(name = "movie_status_active", nullable = false)
    private boolean movieStatusActive;

    @Column(name = "movie_base_price")
    @Min(0)
    @Max(100)
    private double movieBasePrice;

    @ManyToOne
    @NotNull
    private ScreeningRoom screeningRoom;

    // Constructors

    public Movie() {
    }

    public Movie(UUID movieID, String movieTitle, double movieBasePrice, ScreeningRoom screeningRoom) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieBasePrice = movieBasePrice;
        this.movieStatusActive = true;
        this.screeningRoom = screeningRoom;
    }

    // Getters

    public UUID getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public boolean isMovieStatusActive() {
        return movieStatusActive;
    }

    public ScreeningRoom getScreeningRoom() {
        return screeningRoom;
    }

    public double getMovieBasePrice() {
        return movieBasePrice;
    }

    // Setters

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieBasePrice(double movieBasePrice) {
        this.movieBasePrice = movieBasePrice;
    }

    public void setMovieStatusActive(boolean movieStatusActive) {
        this.movieStatusActive = movieStatusActive;
    }

    // Other methods

    public String getMovieInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Movie title: ")
                .append(this.movieTitle)
                .append(", ")
                .append(" location of the spectacle: ")
                .append(this.screeningRoom.getScreeningRoomInfo());
        return stringBuilder.toString();
    }
}
