package dao;

import java.util.List;

public interface EmailAddressDao {

    void save(List<String> email);

    void switchSentStatus(String email);

    List<String> getAll();

    void removeSent();
}
