package com.usermanagement.service.user.service;

import com.usermanagement.service.user.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    boolean deleteUser(Long id);
}
