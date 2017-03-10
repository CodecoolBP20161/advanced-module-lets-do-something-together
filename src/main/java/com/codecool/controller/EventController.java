package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.model.event.Coordinates;
import com.codecool.model.event.Event;
import com.codecool.repository.EventRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Arrays;
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
    public String createEvent(@RequestBody String data, Principal principal) throws JSONException, IOException, IllegalAccessException {
        JSONObject jsonData = new JSONObject(data);
        Event event = new Event();
        for (Field field : getBasicEventFields()) {
            field.setAccessible(true);
            Object fieldValue = jsonData.get(field.getName()).toString();

            if (field.getType().equals(Coordinates.class)) {
                fieldValue = new Coordinates(jsonData.get("lng"), jsonData.get("lng"));
            } else if (field.getType().equals(int.class)) {
                fieldValue = Integer.parseInt(fieldValue.toString());
            }

            field.set(event, fieldValue);
        }
        event.setInterest(interestRepository.findByActivity(jsonData.get("interest").toString()));
        User user = userService.getUserByEmail(principal.getName()).get();
        event.setUser(user);
        eventRepository.save(event);
        return "create_event";
    }

    //    foreign key fields handled separately
    private List<Field> getBasicEventFields() {
        Field[] fieldsArray = Event.class.getDeclaredFields();
        return Arrays.asList(fieldsArray).subList(1, 6);
    }
}
