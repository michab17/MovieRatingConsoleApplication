package com.app.DAO;

import com.app.JDBC.ConnectionManager;
import com.app.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RatingDao {

    private final Connection conn = ConnectionManager.getConnection();

    public Double getAverageRating(int movieId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT AVG(score) as averageScore FROM rating WHERE movie_id = ?");
            ps.setInt(1, movieId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("averageScore");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1.0;
    }

    public Integer getNumOfRatings(int movieId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(movie_id) as numOfRatings FROM rating WHERE movie_id = ?");
            ps.setInt(1, movieId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("numOfRatings");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean changeRating(Double rating, int movieId, String email) {
        try {
            PreparedStatement ps = conn.prepareStatement("update rating r join account a on r.account_id = a.account_id set score = ? where r.movie_id = ? and a.email = ?");
            ps.setDouble(1, rating);
            ps.setInt(2, movieId);
            ps.setString(3, email);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean rateMovie(Double rating, int movieId, String email) {
        try {
            PreparedStatement ps = conn.prepareStatement("insert into rating (score, account_id, movie_id) select ?, a.account_id, m.movie_id from account a join movie m on m.movie_id = ? where a.email = ?");
            ps.setDouble(1, rating);
            ps.setInt(2, movieId);
            ps.setString(3, email);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRating(int movieId, String email) {
        try {
            PreparedStatement ps = conn.prepareStatement("delete r from rating r join account a on r.account_id = a.account_id where r.movie_id = ? and a.email = ?;");
            ps.setInt(1, movieId);
            ps.setString(2, email);

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
