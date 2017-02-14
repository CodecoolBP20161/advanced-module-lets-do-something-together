package com.codecool.security.service.user;

import com.codecool.model.User;
import com.codecool.repository.RoleRepository;
import com.codecool.repository.UserRepository;
import com.codecool.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserServiceImpl() {
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override

    public void create(User user, Role role) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }
}
