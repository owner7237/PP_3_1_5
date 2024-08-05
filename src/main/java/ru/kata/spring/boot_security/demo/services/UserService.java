package ru.kata.spring.boot_security.demo.services;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    @Transactional
    void saveUser(User user);

    @Transactional
    void deleteUser(User user);

    User findByEmail(String email);

    User findById(Long id);

    List<User> userList();
}
