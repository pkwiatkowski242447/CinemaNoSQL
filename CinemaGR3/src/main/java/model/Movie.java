package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
public class Movie {

    @Id
    @Column(nullable = false, unique = true)
    private UUID movieID;

    @Column(nullable = false)
    @Length(min = 1, max = 150)
    private String movieTitle;

    @Column(nullable = false)
    private boolean movieStatusActive;

    @ManyToOne
    @NotNull
    private ScreeningRoom screeningRoom;

    // Constructors

    public Movie() {
    }

    public Movie(UUID movieID, String movieTitle, ScreeningRoom screeningRoom) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
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

    // Setters


    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieStatusActive(boolean movieStatusActive) {
        this.movieStatusActive = movieStatusActive;
    }

    // Other methods

    public String getMovieInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Film pt. ")
                .append(this.movieTitle)
                .append(", ")
                .append(this.screeningRoom.getScreeningRoomInfo());
        return stringBuilder.toString();
    }
}
