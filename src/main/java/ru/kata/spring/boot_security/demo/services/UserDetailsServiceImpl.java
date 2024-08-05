package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ru.kata.spring.boot_security.demo.models.User user = userRepository.findByEmail(email);
        return new User(user.getEmail(), user.getPassword(), authorities(user));
    }

    private Collection<? extends GrantedAuthority> authorities(ru.kata.spring.boot_security.demo.models.User user) {
        return user.getRoles();
    }
}
