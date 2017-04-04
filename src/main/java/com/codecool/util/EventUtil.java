package com.codecool.util;

import com.codecool.model.event.Event;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class EventUtil {

    private static final Logger logger = LoggerFactory.getLogger(EventUtil.class);

    public JSONArray createEventsJson(List<Event> events){
        JSONArray json = new JSONArray();
        logger.info("createEventsJson method called.");
        if (events.size() > 0) {
            for (Event event : events) {
                try {
                    json.put(new JSONObject().put(String.valueOf(event.getId()), createJsonFromEvent(event)));
                } catch (JSONException e) {
                    logger.error("{} occurred while creating json from events: {}", e.getCause(), e.getMessage());
                }
            }
        }
        return json;
    }

    private JSONObject createJsonFromEvent(Event event) {
        logger.info("createJsonFromEvent method called.");
        JSONObject json = new JSONObject();
        List<Field> fields = Arrays.asList(Event.class.getDeclaredFields()).subList(1, 6);
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                json.put(field.getName(), field.get(event));
            }
            json.put("interest", event.getInterest().getActivity());
            json.put("lat", event.getCoordinates().getLat());
            json.put("lng", event.getCoordinates().getLng());
        } catch (JSONException | IllegalAccessException e) {
            logger.error("{} occurred while creating json from event: {}", e.getCause(), e.getMessage());
        }
        return json;
    }

}
