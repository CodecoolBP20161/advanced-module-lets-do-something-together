package com.codecool.config;

import com.codecool.model.Interest;
import com.codecool.model.Profile;
import com.codecool.model.User;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DataLoader {

    private Map<String, String> basicActivities = new HashMap<String, String>() {{
        put("waterSports", "#1E26B8");
        put("winterSports", "#45CCFF");
        put("outdoorSports", "#FF9C2D");
        put("cultural", "#530E53");
        put("outdoorActivity", "#64F22A");
        put("ballGames", "#F22799");
        put("boardGames", "#F03F20");
        put("eSports", "#B243FF");
        put("indoorSports", "#FFFC44");
        put("other", "#32333D");
    }};

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @PostConstruct
    public void loadBasicActivities() {
        if (interestRepository.findAll().isEmpty()) {
            basicActivities.entrySet().forEach(activity -> {
                Interest interest = new Interest(activity.getKey());
                interest.setColorCode(activity.getValue());
                interestRepository.save(interest);
            });
        }
    }

    @PostConstruct
    public void loadAdmin() {
        if (userService.getUserByEmail("admin@admin.com").equals(Optional.empty())) {
            User admin = new User("admin@admin.com", "1234");
            userService.create(admin, Role.ADMIN);
            Profile profile = new Profile(admin);
            profileRepository.save(profile);
        }
    }
}