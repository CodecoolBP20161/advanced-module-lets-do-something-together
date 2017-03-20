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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DataLoader {

    private List<String> basicActivities = new ArrayList<>(
            Arrays.asList("tennis", "gokart", "running", "cardGames",
                    "cinema", "theater", "cityWalks", "hiking"));

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @PostConstruct
    public void loadBasicActivities() {
        if (interestRepository.findAll().isEmpty()) {
            basicActivities.forEach(activity -> interestRepository.save(new Interest(activity)));
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