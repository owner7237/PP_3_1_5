package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private UserService userService;

    private RoleService roleService;
    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping(value = "/add")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(userService.findById(id));
        return "redirect:/admin";
    }
    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping()
    public String adminPageBoot(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("currentUser", userService.findByEmail(principal.getName()));
        modelMap.addAttribute("users", userService.userList());
        modelMap.addAttribute("allRoles", roleService.roleList());
        return "adminPage";
    }
}
