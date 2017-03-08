package com.codecool.repository;

import com.codecool.model.Event;
import com.codecool.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository {

    List<Event> findByDate(String date);
    List<Event> findByLocation(String location);
    List<Event> findByParticipants(Integer number);
    List<Event> findByCategory(Interest category);
    List<Event> findByStatus(String status);
}
