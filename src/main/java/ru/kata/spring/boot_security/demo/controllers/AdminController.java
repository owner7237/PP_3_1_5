package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired

    private RoleRepository roleRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @GetMapping()
    public String adminPage(ModelMap modelMap) {
        modelMap.addAttribute("userList", userService.userList());
        modelMap.addAttribute("allRoles", roleRepository.findAll());
        return "users";
    }
    @PostMapping(value = "/add")
    public String addUser(@ModelAttribute("newuser") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(userService.findById(id));
        return "redirect:/admin";
    }
    @GetMapping(value = "/edit")
    public String editUserPage(@RequestParam("id") Long id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.findById(id));
        modelMap.addAttribute("allRoles", roleRepository.findAll());
        return "user-edit";
    }
    @PostMapping(value = "/edit/submit")
    public String editUser(@ModelAttribute User user) {
        User originalUser = userService.findById(user.getId());
        if (user.getPassword().isEmpty()){
            user.setPassword(originalUser.getPassword());
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }
}
