package com.app.model;

public class Movie {
    private int movieId;
    private String movieName;
    private Double averageRating;
    private Integer numOfRatings;

    public Movie(int movieId, String movieName, Double averageRating, Integer numOfRatings) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.averageRating = averageRating;
        this.numOfRatings = numOfRatings;
    }

    public int getMovieId() {
        return movieId;
    }
    public String getMovieName() {
        return movieName;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getNumOfRatings() {
        return numOfRatings;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", averageRating=" + averageRating +
                ", numOfRatings=" + numOfRatings +
                '}';
    }
}
