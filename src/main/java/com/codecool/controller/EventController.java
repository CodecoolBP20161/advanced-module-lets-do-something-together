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
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
    @ResponseBody
    public String createEvent(@RequestBody String data, Principal principal) throws JSONException {
        JSONObject result = new JSONObject().put("status", "failed");

        Event event = new Event();
        JSONObject json = new JSONObject(data);
        if (validateEventJson(json)) {
            event.setParticipants(Integer.parseInt(json.get("participants").toString()));
            event.setDescription(json.get("description").toString());
            event.setName(json.get("name").toString());
            event.setCoordinates(new Coordinates(json.get("lng"), json.get("lat")));
            event.setLocation(json.get("location").toString());
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                event.setDate(format.parse(json.get("date").toString()));
            } catch (ParseException e) {
                e.getMessage();
            }
            event.setInterest(interestRepository.findByActivity(json.get("interest").toString()));
            event.setUser(userService.getUserByEmail(principal.getName()).get());
            eventRepository.save(event);

            result.put("status", "success");
        }
        return result.toString();
    }

    private boolean validateEventJson(JSONObject json) {
        List<Field> fields = Arrays.asList(Event.class.getDeclaredFields()).subList(1, 7);
        return !fields.stream().map(field -> json.has(field.getName())).collect(Collectors.toList()).contains(false);
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
