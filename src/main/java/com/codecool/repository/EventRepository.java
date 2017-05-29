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

    List<Event> findByStatus(Status status);

    List<Event> findByUserAndStatus(User user, Status status);

    List<Event> findByStatusAndInterestInOrderByDate(Status status, List<Interest> interest);

    List<Event> findByStatusAndInterestInAndLocationIgnoreCaseContainingOrderByDate(
            Status status, List<Interest> interest, String location);

    List<Event> findByStatusAndInterestInAndDateBetweenOrderByDate(
            Status status, List<Interest> interest, Date startDate, Date endDate);

    List<Event> findByStatusAndInterestInAndDateBetweenAndLocationIgnoreCaseContainingOrderByDate(
            Status status, List<Interest> interest, Date startDate, Date endDate, String location);

    Event findById(Integer id);
}