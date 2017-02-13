package com.codecool.security.service.currentuser;

import com.codecool.model.CurrentUser;
import com.codecool.security.Role;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, int userId) {
        return currentUser != null && (currentUser.getRole().contains(Role.ADMIN) || currentUser.getId() == userId);
    }
}
