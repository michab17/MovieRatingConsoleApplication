package com.app.DAO;

import com.app.JDBC.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    private static final Connection conn = ConnectionManager.getConnection();

    public boolean checkCredentials(String email, String password) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM account WHERE email = ? AND password = ?");
            st.setString(1, email);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();

            if(rs.next()) {
                rs.close();
                st.close();

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean createAccount(String email, String password, boolean isAdmin) {

        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO account(email, password, isAdmin) values (?, ?, ?)");
            st.setString(1, email);
            st.setString(2, password);
            st.setBoolean(3, isAdmin);

            st.execute();

            st.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isAdmin(String email) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT isAdmin FROM account WHERE email = ?");
            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("isAdmin") == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
