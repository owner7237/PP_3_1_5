package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
public class AdminRestController {
    private UserService userService;

    private RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ModelAndView adminPageBoot(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("adminPage");
        modelAndView.addObject("currentUser", userService.findByEmail(principal.getName()));
        modelAndView.addObject("allRoles", roleService.roleList());
        return modelAndView;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.userList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/api/users")
    public ResponseEntity editUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/users")
    public ResponseEntity addUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(userService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
