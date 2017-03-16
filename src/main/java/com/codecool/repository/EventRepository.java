package com.codecool.repository;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByDate(Date date);

    List<Event> findByLocation(String location);

    List<Event> findByParticipants(Integer number);

    List<Event> findByInterest(Interest interest);

    List<Event> findByStatus(Status status);

    List<Event> findByUser(User user);
}
