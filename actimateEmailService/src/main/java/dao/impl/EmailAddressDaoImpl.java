package dao.impl;

import dao.DbHandler;
import dao.EmailAddressDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                PreparedStatement query = connection.prepareStatement("INSERT INTO email_address (email, sent) VALUES (?, ?);");
                query.setString(1, email);
                query.setBoolean(2, false);
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
    public List<String> getAllNew() {
        return getAll(false);
    }

    @Override
    public List<String> getAllSent() {
        return getAll(true);
    }

    private List<String> getAll(boolean sent) {
        List<String> addresses = new ArrayList<>();
        try (Connection connection = getConnection()
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM email_address WHERE sent = ? ;");
            preparedStatement.setBoolean(1, sent);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                addresses.add(rs.getString("email"));
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
