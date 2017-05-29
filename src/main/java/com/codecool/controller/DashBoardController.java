package com.codecool.controller;

import com.codecool.model.Profile;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@RequestMapping(value = "/u")
@Controller
public class DashBoardController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @ResponseBody
    public String collectEvents(Principal principal) {
        logger.info("/u/events route called - method: {}.", RequestMethod.GET);
        JSONObject events = new JSONObject();
        try {
            events.put("events", getAllEvents(principal));
            logger.info("All events collected to dashboard.");
        } catch (JSONException e) {
            logger.error("{} occurred while collecting events to json: {}", e.getCause(), e.getMessage());
        }
        return events.toString();
    }

    private JSONArray getAllEvents(Principal principal) {
        Profile profile = getCurrentProfile(principal);
        List<Event> ownEvents = eventRepository.findByStatusAndInterestInOrderByDate(Status.ACTIVE, profile.getInterestList());
        logger.info("{} active events based on user's interest(s) collected.", ownEvents.size());
        List<Event> otherEvents = eventRepository.findByStatus(Status.ACTIVE);
        logger.info("{} other active events collected.", otherEvents.size());
        List<Event> events = mergeLists(ownEvents, otherEvents);
        return eventUtil.createEventsJson(events);
    }

    private List<Event> mergeLists(List<Event> ownEvents, List<Event> otherEvents) {
        otherEvents.removeAll(ownEvents);
        ownEvents.addAll(otherEvents);
        logger.info("Interest based and every other event merged. {} event(s) collected.", ownEvents.size());
        return ownEvents;
    }

    @RequestMapping(value = "/event/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String showCurrentEvent(@PathVariable(value = "id") String id) throws JSONException {
        logger.info("/u/event/{} route called - method: {}", id, RequestMethod.GET);
        try {
            Event event = eventRepository.findById(Integer.parseInt(id));
            logger.info("Event (id: {}) has found", id);
            return eventUtil.createJsonFromEvent(event).toString();
        } catch (NullPointerException e) {
            logger.error("{} occurred while looking for event with id:{}; {}", e.getClass().getSimpleName(), id, e.getMessage());
            JSONObject message = new JSONObject();
            message.put("message", "No event has found with this id");
            return message.toString();
        }
    }
}
