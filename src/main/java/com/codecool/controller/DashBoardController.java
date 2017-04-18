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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@RequestMapping(value = "/u")
@Controller
public class DashBoardController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        logger.info("/u/dashboard route called - method: {}.", RequestMethod.GET);

        return "event_feed";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @ResponseBody
    public String collectEvents() {
        logger.info("/u/events route called - method: {}.", RequestMethod.GET);
        JSONObject events = new JSONObject();
        try {
            events.put("events", getAllEvents());
            logger.info("All events collected to dashboard.");
        } catch (JSONException e) {
            logger.error("{} occurred while collecting events to json: {}", e.getCause(), e.getMessage());
        }
        return events.toString();
    }

    //    TODO initial version for test purposes
    @RequestMapping(value = "/custom_events", method = RequestMethod.GET)
    @ResponseBody
    public String getCustomEventsBasedOnInterest(Principal principal) {
        Profile profile = getCurrentProfile(principal);
        List<Event> events = eventRepository.findByStatusAndInterestInOrderByDate(Status.ACTIVE, profile.getInterestList());
        logger.info("{} active events collected based on user's interests.", events.size());
        return eventUtil.createEventsJson(events).toString();
    }

    private JSONArray getAllEvents() {
        List<Event> events = eventRepository.findByStatus(Status.ACTIVE);
        logger.debug("{} active events collected.", events.size());
        return eventUtil.createEventsJson(events);
    }

}
