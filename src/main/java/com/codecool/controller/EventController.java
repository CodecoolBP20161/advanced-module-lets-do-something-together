package com.codecool.controller;

import com.codecool.model.event.Coordinates;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @RequestMapping(value = "/create_event", method = RequestMethod.GET)
    public String renderCreateEventForm() {
        logger.info("/u/create_event route called - method: {}.", RequestMethod.GET);
        return "create_event";
    }

    @RequestMapping(value = "/create_event", method = RequestMethod.POST)
    @ResponseBody
    public String createEvent(@RequestBody String data, Principal principal) throws JSONException {
        logger.info("/u/create_event route called - method: {}.", RequestMethod.POST);
        logger.debug("Create event data: {}", data);
        JSONObject result = new JSONObject().put("status", "failed");
        Event event = new Event();
        JSONObject json = new JSONObject(data);
        if (validateEventJson(json)) {
            logger.info("Valid json, all necessary event fields present.");
            event.setParticipants(Integer.parseInt(json.get("participants").toString()));
            event.setDescription(json.get("description").toString());
            event.setName(json.get("name").toString());
            event.setCoordinates(new Coordinates(json.get("lng"), json.get("lat")));
            event.setLocation(json.get("location").toString());
            event.setDate(parseDate(json.get("date").toString()));
            event.setInterest(interestRepository.findByActivity(json.get("interest").toString()));
            event.setUser(userService.getUserByEmail(principal.getName()).get());
            eventRepository.save(event);
            logger.info("New event created successfully.");
            result.put("status", "success");
        }
        logger.error("Invalid json, not all necessary event fields present.");
        return result.toString();
    }

    private boolean validateEventJson(JSONObject json) {
        logger.info("Validating event json: {}", json);
        List<Field> fields = Arrays.asList(Event.class.getDeclaredFields()).subList(1, 7);
        return !fields.stream().map(field -> json.has(field.getName())).collect(Collectors.toList()).contains(false);
    }

    private Date parseDate(String date) {
        Date parsedDate = null;
        try {
            DateFormat format = new SimpleDateFormat("MM/dd/yyy hh:mm a");
            parsedDate = format.parse(date);
            logger.info("'{}' successfully parsed from event json", parsedDate);
        } catch (ParseException e) {
            logger.error("{} occurred while parsing the date of the event: {}.\n" +
                    "Unparsable date: {}", e.getCause(), e.getMessage(), date);
        }
        return parsedDate;
    }

    //    method called daily at midnight
//    toggles status of every event before that
    @Scheduled(cron = "0 0 0 * * ?", zone = "CET")
    private void managePastEvents() {
        logger.info("Midnight db management: updating events' statuses.");
        List<Event> events = eventRepository.findByStatus(Status.ACTIVE);
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
        logger.info("Compare dates: {} to {}", date, today);
        return today.compareTo(date);
    }
}
