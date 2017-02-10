package com.codecool.repository;

import com.codecool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository {
    User findByEmail(String email);
}