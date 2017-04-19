package com.codecool.email.repository;

import com.codecool.email.model.WelcomeEmail;
import com.codecool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WelcomeEmailRepository extends JpaRepository<WelcomeEmail, Integer> {

    List<WelcomeEmail> findAllByEmailSent(boolean emailSent);

    WelcomeEmail findByUser(User user);

}
