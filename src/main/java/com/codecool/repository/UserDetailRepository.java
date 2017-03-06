package com.codecool.repository;


import com.codecool.model.User;
import com.codecool.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    UserDetail findByUser(User user);
}