package com.codecool.util;

import com.codecool.model.event.Event;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EventUtil {

    private static final Logger logger = LoggerFactory.getLogger(EventUtil.class);

    private Calendar calendar = Calendar.getInstance();

    public JSONObject createEventsJson(List<Event> events) {
        JSONObject json = new JSONObject();
        logger.info("createEventsJson method called.");
        if (events.size() > 0) {
            for (Event event : events) {
                try {
                    json.put(String.valueOf(event.getId()), createJsonFromEvent(event));
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
