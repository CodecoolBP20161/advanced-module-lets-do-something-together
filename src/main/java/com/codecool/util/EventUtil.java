package com.codecool.util;

import com.codecool.model.event.Event;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class EventUtil {

    public JSONObject createEventsJson(List<Event> events) {
        JSONObject json = new JSONObject();
        if (events.size() > 0) {
            for (Event event : events) {
                try {
                    json.put(String.valueOf(event.getId()), createJsonFromEvent(event));
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        }
        return json;
    }

    private JSONObject createJsonFromEvent(Event event) {
        JSONObject json = new JSONObject();
        List<Field> fields = Arrays.asList(Event.class.getDeclaredFields()).subList(1, 7);
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                json.put(field.getName(), field.get(event));
            }
            json.put("lat", event.getCoordinates().getLat());
            json.put("lng", event.getCoordinates().getLng());
        } catch (JSONException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return json;
    }

}
