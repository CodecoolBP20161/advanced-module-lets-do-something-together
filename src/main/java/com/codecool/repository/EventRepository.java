package com.codecool.repository;

import com.codecool.model.Interest;
import com.codecool.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByDate(String date);

    List<Event> findByLocation(String location);

    List<Event> findByParticipants(Integer number);

    List<Event> findByInterest(Interest category);

    List<Event> findByStatus(String status);
}
