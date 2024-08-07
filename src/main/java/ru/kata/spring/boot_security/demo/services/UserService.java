package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    void deleteUser(User user);
    void deleteUserById(Long id);

    User findByEmail(String email);

    User findById(Long id);

    List<User> userList();
}
