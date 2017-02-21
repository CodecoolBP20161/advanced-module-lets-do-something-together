package com.codecool.security.service.currentuser;

import com.codecool.model.CurrentUser;

public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, int userId);
}
