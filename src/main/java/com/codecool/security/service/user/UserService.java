package com.codecool.security.service.user;

import com.codecool.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(int id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(User user);

}
