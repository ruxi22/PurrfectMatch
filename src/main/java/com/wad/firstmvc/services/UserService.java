package com.wad.firstmvc.services;

import com.wad.firstmvc.domain.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    void save(User user);
}
