package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.Profile;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import org.json.JSONException;
import org.json.JSONObject;
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

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String saveProfileForm(@RequestBody String data, Principal principal) throws JSONException, IllegalAccessException {
        Profile currentProfile = getCurrentProfile(principal);
        List<Field> fields = getEditableFieldsOfCurrentProfile(currentProfile);

//            profile related JSONExceptions swallowed on purpose: not mandatory profile details
        JSONObject jsonData = new JSONObject(data);

        for (Field field : fields.subList(0, fields.size() - 1)) {
            field.setAccessible(true);
            String fieldValue = null;
            try {
                fieldValue = jsonData.get(field.getName()).toString();
            } catch (JSONException ignored) {
            }
            field.set(currentProfile, fieldValue);
        }
        currentProfile.setInterestList(getUpdatedInterestList(currentProfile, jsonData));
        profileRepository.save(currentProfile);
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

    private JSONObject getUserEvents(Principal principal) {
        List<Event> events = eventRepository.findByUserAndStatus(getCurrentUser(principal), Status.ACTIVE);
        return eventUtil.createEventsJson(events);
    }
}