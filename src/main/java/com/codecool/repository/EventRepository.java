package com.codecool.repository;

import com.codecool.model.Interest;
import com.codecool.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventRepository extends JpaRepository {

    List<Event> findByDate(String date);
    List<Event> findByLocation(String location);
    List<Event> findByParticipants(Integer number);
    List<Event> findByCategory(Interest category);
    List<Event> findByStatus(String status);
}
