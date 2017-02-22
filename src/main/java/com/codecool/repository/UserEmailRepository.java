package com.codecool.repository;

import com.codecool.model.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, Integer> {
    List<UserEmail> findAllByEmailSent(boolean emailSent);
}
