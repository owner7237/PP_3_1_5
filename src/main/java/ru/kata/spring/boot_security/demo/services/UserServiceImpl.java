package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user.getPassword().isEmpty()) {
            user.setPassword(userRepository.findById(user.getId()).orElseThrow().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.getRoles().forEach(role -> role.setName(roleRepository.getById(role.getId()).getName()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with ID: %s not found.", id)));
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }
}
