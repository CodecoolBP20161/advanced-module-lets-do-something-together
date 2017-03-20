package com.codecool.controller;

import com.codecool.model.event.Coordinates;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import com.codecool.repository.EventRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@RequestMapping(value = "/u")
@Controller
public class EventController extends AbstractController {

    @Autowired
    EventRepository eventRepository;

    @RequestMapping(value = "/create_event", method = RequestMethod.GET)
    public String renderCreateEventForm() {
        return "create_event";
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    public String createEvent(@RequestBody String data, Principal principal) throws JSONException {
        JSONObject jsonData = new JSONObject(data);
        Event event = new Event();
        event.setParticipants(Integer.parseInt(jsonData.get("participants").toString()));
        event.setDescription(jsonData.get("description").toString());
        event.setName(jsonData.get("name").toString());
        event.setLocation(new Coordinates(jsonData.get("lng"), jsonData.get("lat")));
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            event.setDate(format.parse(jsonData.get("date").toString()));
        } catch (ParseException e) {
            e.getMessage();
        }
        event.setInterest(interestRepository.findByActivity(jsonData.get("interest").toString()));
        event.setUser(userService.getUserByEmail(principal.getName()).get());
        eventRepository.save(event);
        return "create_event";
    }

    //    method called daily at midnight
//    toggles status of every event before that
    @Scheduled(cron = "0 0 0 * * ?", zone = "CET")
    private void managePastEvents() {
        List<Event> events = eventRepository.findAll();
        events.stream().filter(event -> compareDates(event.getDate()) >= 0).forEach(event -> {
            event.setStatus(Status.PAST);
            eventRepository.save(event);
        });
    }

    //    compares string date to today's last minute
    private int compareDates(Date date) {
        Calendar calendarDay = new GregorianCalendar();
        calendarDay.set(Calendar.HOUR_OF_DAY, 23);
        calendarDay.set(Calendar.MINUTE, 59);
        calendarDay.set(Calendar.SECOND, 59);
        Date today = calendarDay.getTime();
        return today.compareTo(date);
    }
}
