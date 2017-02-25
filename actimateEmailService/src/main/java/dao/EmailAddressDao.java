package dao;

import java.util.List;

public interface EmailAddressDao {

    void save(List<String> email);

    void switchSentStatus(String email);

    List<String> getAllNew();

    List<String> getAllSent();

    void removeSent();
}
