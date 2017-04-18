package com.codecool.util;

import com.codecool.model.Profile;
import com.codecool.model.event.Event;
import com.codecool.repository.ProfileRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventUtil {

    private static final Logger logger = LoggerFactory.getLogger(EventUtil.class);

    private Calendar calendar = Calendar.getInstance();

    @Autowired
    ProfileRepository profileRepository;

    public JSONArray createEventsJson(List<Event> events) {
        JSONArray json = null;
        logger.info("createEventsJson method called.");
        if (events.size() > 0) {
            List<JSONObject> js = events
                    .stream()
                    .map(this::createJsonFromEvent)
                    .collect(Collectors.toList());
            json = new JSONArray(js);
        }
        return json;
    }

    private JSONObject createJsonFromEvent(Event event) {
        logger.info("createJsonFromEvent method called for event 'id_{}'.", event.getId());
        JSONObject json = new JSONObject();
        List<Field> fields = Arrays.asList(Event.class.getDeclaredFields()).subList(1, 6);
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                json.put(field.getName(), field.get(event));
            }
            json.put("interest", event.getInterest().getActivity());
            json.put("color", event.getInterest().getColorCode());
            json.put("lat", event.getCoordinates().getLat());
            json.put("lng", event.getCoordinates().getLng());
            json.put("user", getEventOwnerName(event));
        } catch (JSONException | IllegalAccessException e) {
            logger.error("{} occurred while creating json from event: {}", e.getCause(), e.getMessage());
        }
        return json;
    }
    private String getEventOwnerName(Event event) {
        Profile profile = profileRepository.findByUser(event.getUser());
        return (profile.getLastName() != null) ?
                String.format("%s %s", profile.getFirstName(), profile.getLastName()) :
                profile.getFirstName().split("@")[0];
    }

    public Date getDateOneMonthFromNow(Date now) {
        logger.info("Getting date one month from now.");
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    //    compares string date to today's last minute
    public int compareDates(Date date) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date today = calendar.getTime();
        logger.info("Compare dates: {} to {}", date, today);
        return today.compareTo(date);
    }

}
