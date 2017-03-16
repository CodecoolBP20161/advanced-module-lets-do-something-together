package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.codecool.model.UserDetail;
import com.codecool.model.event.Event;
import com.codecool.repository.EventRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping(value = "/u")
@Controller
public class ProfileController extends AbstractController {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String renderProfile() {
        return "profile";
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public String renderProfileForm() {
        return "profile_form";
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String saveProfileForm(@RequestBody String data, Principal principal) throws JSONException, IllegalAccessException {
        UserDetail currentUserDetail = getCurrentUserDetail(principal);
        List<Field> fields = getEditableFieldsOfCurrentUserDetail(currentUserDetail);

//            profile related JSONExceptions swallowed on purpose: not mandatory profile details
        JSONObject jsonData = new JSONObject(data);

        for (Field field : fields.subList(0, fields.size() - 1)) {
            field.setAccessible(true);
            String fieldValue = null;
            try {
                fieldValue = jsonData.get(field.getName()).toString();
            } catch (JSONException ignored) {
            }
            field.set(currentUserDetail, fieldValue);
        }
        currentUserDetail.setInterestList(getInterestsFromJson(jsonData));
        userDetailRepository.save(currentUserDetail);
        return "profile_form";
    }

    @RequestMapping(value = "/profile_data", method = RequestMethod.GET)
    @ResponseBody
    public String profileData(Principal principal) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("profile", getUserProfile(principal));
            jsonObject.put("events", getUserEvents(principal));
        } catch (JSONException e) {
            e.getMessage();
        }
        return jsonObject.toString();
    }

    private JSONObject getUserProfile(Principal principal) throws IllegalAccessException {
        JSONObject json = new JSONObject();
        UserDetail currentUserDetail = getCurrentUserDetail(principal);
        List<Field> fields = getEditableFieldsOfCurrentUserDetail(currentUserDetail);
        for (Field field : fields.subList(0, fields.size() - 1)) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(currentUserDetail);
                json.put(field.getName(), fieldValue);
            } catch (JSONException ignored) {
            }
        }
        try {
            json.put(fields.get(fields.size() - 1).getName(),
                    currentUserDetail.getInterestList().stream().map(Interest::getActivity).collect(Collectors.toList()));
        } catch (JSONException ignored) {
        }
        return json;
    }

    private List<Interest> getInterestsFromJson(JSONObject jsonObject) {
        List<Interest> interestList = new ArrayList<>();
        try {
            JSONArray jsonArray = ((JSONObject) jsonObject.get("interest")).names();
            for (int i = 0; i < jsonArray.length(); i++) {
                interestList.add(interestRepository.findByActivity(jsonArray.getString(i)));
            }
        } catch (JSONException ignored) {
        }
        return interestList;
    }

    private List<Field> getEditableFieldsOfCurrentUserDetail(UserDetail userDetail) {
        Field[] fieldsArray = userDetail.getClass().getDeclaredFields();
        return Arrays.asList(fieldsArray).subList(2, fieldsArray.length);
    }

    private JSONObject getUserEvents(Principal principal) {
        JSONObject json = new JSONObject();
        User user = getCurrentUser(principal);
        List<Event> events = eventRepository.findByUser(user);
        if (events.size() > 0) {
            for (int i = 0; i < events.size(); i++) {
                try {
                    json.put(String.valueOf(i), createEventJson(events.get(i)));
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        }
        return json;
    }

    private JSONObject createEventJson(Event event) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", event.getName());
            json.put("lat", event.getLocation().getLat());
            json.put("lng", event.getLocation().getLng());
            json.put("date", event.getDate());
            json.put("participants", event.getParticipants());
            json.put("description", event.getDescription());
            json.put("interest", event.getInterest().getActivity());
        } catch (JSONException e) {
            e.getMessage();
        }
        return json;
    }
}