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
                logger.error("{}: occurred while trying to parse data {}", ignored.getClass().getSimpleName(), ignored.getMessage());
                result.put("status","error");
            }
            field.set(currentProfile, fieldValue);
        }
        currentProfile.setInterestList(getUpdatedInterestList(currentProfile, jsonData));
        profileRepository.save(currentProfile);
        logger.info("Profile(email: {}) saved into the database", currentProfile.getUser().getEmail());
        result.put("status","success");
        return result.toString();
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
        }
        return json;
    }

    private List<Interest> getUpdatedInterestList(Profile profile, JSONObject jsonData) {
        List<Interest> interests = profile.getInterestList();
        try {
            JSONObject json = (JSONObject) jsonData.get("interest");
            for (Interest interest : interestRepository.findAll()) {
                String activity = interest.getActivity();
                if (json.has(activity)) {
                    if (json.get(activity).equals(false)) {
                        interests.remove(interest);
                    } else if (!interests.contains(interest)) {
                        interests.add(interest);
                    }
                }
            }
        } catch (JSONException ignored) {
        }
        return interests;
    }

    private List<Field> getEditableFieldsOfCurrentProfile(Profile profile) {
        Field[] fieldsArray = profile.getClass().getDeclaredFields();
        return Arrays.asList(fieldsArray).subList(2, fieldsArray.length);
    }

    private JSONArray getUserEvents(Principal principal) {
        List<Event> events = eventRepository.findByUserAndStatus(getCurrentUser(principal), Status.ACTIVE);
        return eventUtil.createEventsJson(events);
    }
}