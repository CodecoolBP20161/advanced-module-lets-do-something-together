package com.codecool.controller;

import com.codecool.model.event.Event;
import com.codecool.repository.EventRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@RequestMapping(value = "/u")
@Controller
public class DashBoardController extends AbstractController {

    @Autowired
    EventRepository eventRepository;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userMain() {
        return "user_main";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @ResponseBody
    public String collectEvents(Principal principal) {
        JSONObject events = new JSONObject();
        try {
            events.put("events", getAllEvents());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events.toString();
    }

    private JSONObject getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventUtil.createEventsJson(events);
    }

}
