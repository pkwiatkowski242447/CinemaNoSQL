package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class Movie {
    private final UUID movieID;
    private String movieTitle;
    private boolean movieStatusActive;
    private double movieBasePrice;
    private final ScreeningRoom screeningRoom;

    // Constructors

    public Movie(UUID movieID, String movieTitle, boolean movieStatusActive, double movieBasePrice, ScreeningRoom screeningRoom) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieStatusActive = movieStatusActive;
        this.movieBasePrice = movieBasePrice;
        this.screeningRoom = screeningRoom;
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
        stringBuilder.append("Movie id: ")
                .append(this.movieID)
                .append(", movie title: ")
                .append(this.movieTitle)
                .append(", location of the spectacle: ")
                .append(" ; " + this.screeningRoom.getScreeningRoomInfo());
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return new EqualsBuilder()
                .append(movieID, movie.movieID)
                .append(movieTitle, movie.movieTitle)
                .append(movieBasePrice, movie.movieBasePrice)
                .append(movieStatusActive, movie.movieStatusActive)
                .append(screeningRoom, movie.screeningRoom)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(movieID)
                .append(movieTitle)
                .append(movieStatusActive)
                .append(movieBasePrice)
                .append(screeningRoom)
                .toHashCode();
    }
}
