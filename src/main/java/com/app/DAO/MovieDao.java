package com.app.DAO;

import com.app.JDBC.ConnectionManager;
import com.app.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieDao {
    RatingDao ratingDao = new RatingDao();
    private final Connection conn = ConnectionManager.getConnection();

    public List<Movie> makeList(ResultSet rs) {
        List<Movie> movies = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);

                movies.add(new Movie(id, name, ratingDao.getAverageRating(id), ratingDao.getNumOfRatings(id)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movies;
    }
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM movie");
            ResultSet rs = ps.executeQuery();

            movies = makeList(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<Movie> getRatedMovies(String email) {
        List<Movie> movies = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("select m.movie_id, name from rating r join movie m on r.movie_id = m.movie_id join account a on r.account_id = a.account_id where a.email = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            movies = makeList(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        movies.sort(Comparator.comparing(Movie::getAverageRating).reversed());
        return movies;
    }

    public List<Movie> getUnratedMovies(String email) {
        List<Movie> movies = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("with rated_movies as " +
                    "(select name from rating r join movie m on r.movie_id = m.movie_id join account a on r.account_id = a.account_id where a.email = ?) " +
                    "select * from movie " +
                    "where movie.name not in (select * from rated_movies)");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            movies = makeList(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        movies.sort(Comparator.comparing(Movie::getAverageRating).reversed());
        return movies;
    }

    public double getUserRating(String email, String movieName) {
        double rating = 0.00;

        try {
            PreparedStatement ps = conn.prepareStatement("select score from rating r join account a on r.account_id = a.account_id join movie m on r.movie_id = m.movie_id where a.email = ? and  m.name = ?");
            ps.setString(1, email);
            ps.setString(2, movieName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rating = rs.getDouble("score");
            }

            return rating;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addMovie(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("insert into movie values(null, ?)");
            ps.setString(1, name);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean editMovie(String newName, String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("update movie set name = ? where name = ?");
            ps.setString(1, newName);
            ps.setString(2, name);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
