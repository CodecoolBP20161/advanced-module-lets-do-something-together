package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.Profile;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String renderProfile() {
        return "profile";
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public String renderProfileForm() {
        return "profile_form";
    }

    @ResponseBody
    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String saveProfileForm(@RequestBody String data, Principal principal) throws JSONException, IllegalAccessException {
        logger.info("/u/edit-profile route called - method: {}.", RequestMethod.POST);
        logger.info("Profile data: '{}'", data);
        Profile currentProfile = getCurrentProfile(principal);
        JSONObject result = new JSONObject();
        List<Field> fields = getEditableFieldsOfCurrentProfile(currentProfile);

//            profile related JSONExceptions swallowed on purpose: not mandatory profile details
        JSONObject jsonData = new JSONObject(data);

        for (Field field : fields.subList(0, fields.size() - 1)) {
            field.setAccessible(true);
            String fieldValue = null;
            try {
                fieldValue = jsonData.get(field.getName()).toString();
            } catch (JSONException ignored) {
                logger.error("Ignored {} occurred while trying to parse data {}", ignored.getClass().getSimpleName(), ignored.getMessage());
                result.put("status", "error");
            }
            field.set(currentProfile, fieldValue);
        }
        currentProfile.setInterestList(getUpdatedInterestList(jsonData));
        profileRepository.save(currentProfile);
        logger.info("Profile(email: '{}') saved into the database", currentProfile.getUser().getEmail());
        result.put("status", "success");
        return result.toString();
    }

    @RequestMapping(value = "/profile_data", method = RequestMethod.GET)
    @ResponseBody
    public String profileData(Principal principal) throws IllegalAccessException {
        logger.info("Profile requests data for user '{}'", principal.getName());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("profile", getUserProfile(principal));
            jsonObject.put("events", getUserEvents(principal));
        } catch (JSONException e) {
            logger.error("{}: occurred while trying to collect profile data {}", e.getClass().getSimpleName(), e.getMessage());
        }
        return jsonObject.toString();
    }

    private JSONObject getUserProfile(Principal principal) throws IllegalAccessException {
        logger.info("Profile data collected for user '{}'", principal.getName());
        JSONObject json = new JSONObject();
        Profile currentProfile = getCurrentProfile(principal);
        List<Field> fields = getEditableFieldsOfCurrentProfile(currentProfile);
        try {
            for (Field field : fields.subList(0, fields.size() - 1)) {
                field.setAccessible(true);
                Object fieldValue = field.get(currentProfile);
                json.put(field.getName(), fieldValue);
            }
            json.put(fields.get(fields.size() - 1).getName(),
                    currentProfile
                            .getInterestList()
                            .stream()
                            .map(Interest::getActivity)
                            .collect(Collectors.toList()));
        } catch (JSONException ignored) {
            logger.error("Ignored {}: occurred while trying to collect profile data {}", ignored.getClass().getSimpleName(), ignored.getMessage());
        }
        return json;
    }

    private List<Interest> getUpdatedInterestList(JSONObject jsonData) {
        List<Interest> interests = new ArrayList<>();
        try {
            JSONArray json = (JSONArray) jsonData.get("interest");
            for (int i = 0; i < json.length(); i++) {
                interests.add(interestRepository.findByActivity(json.get(i).toString()));
            }
            logger.info("Create updated interest list. {} interest(s) added", interests.size());
        } catch (JSONException e) {
            logger.error("Ignored {}: occurred while trying to collect profile data {}", e.getClass().getSimpleName(), e.getMessage());
        }
        return interests;
    }

    private List<Field> getEditableFieldsOfCurrentProfile(Profile profile) {
        Field[] fieldsArray = profile.getClass().getDeclaredFields();
        return Arrays.asList(fieldsArray).subList(2, fieldsArray.length);
    }

    private JSONObject getUserEvents(Principal principal) {
        logger.info("Event data collected for user {}", principal.getName());
        List<Event> events = eventRepository.findByUserAndStatus(getCurrentUser(principal), Status.ACTIVE);
        logger.info("{} events collected", events.size());
        return eventUtil.createEventsJson(events);
    }
}