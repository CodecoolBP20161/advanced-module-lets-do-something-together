package dao.impl;

import dao.DbHandler;
import dao.EmailAddressDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailAddressDaoImpl extends DbHandler implements EmailAddressDao {

    private static EmailAddressDaoImpl instance = null;

    public static EmailAddressDaoImpl getInstance() {
        if (instance == null) {
            instance = new EmailAddressDaoImpl();
        }
        return instance;
    }

    @Override
    public void save(List<String> emails) {
        for (String email : emails) {
            try (Connection connection = getConnection()) {
                PreparedStatement query = connection.prepareStatement("INSERT INTO email_address (email, remoteid, sent) VALUES (?, ?, ?);");
                query.setString(1, email);
                query.setInt(2, 0);
                query.setBoolean(3, false);
                query.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void switchSentStatus(String email) {
        try (Connection connection = getConnection()) {
            PreparedStatement query = connection.prepareStatement("UPDATE email_address SET sent = ? WHERE email = ? ;");
            query.setBoolean(1, true);
            query.setString(2, email);
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAll() {
        List<String> addresses = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM email_address;")
        ) {
            while (rs.next()) {
                if (!rs.getBoolean("sent")) {
                    addresses.add(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    public void removeSent() {
        try (Connection connection = getConnection()) {
            PreparedStatement query = connection.prepareStatement("DELETE FROM email_address WHERE sent  = ? ;");
            query.setBoolean(1, true);
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
