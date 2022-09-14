package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/users")
    public String printUsers(Principal principal, ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.returnUserByMail(principal.getName()));
        model.addAttribute("roles", roleService.listAllRoles());
        return "users";
    }

    @PostMapping(value = "/admin/save")
    public String saveUser(@ModelAttribute(value = "user") User user, BindingResult bindingResult, @RequestParam(value = "roles") String[] rol, Model model) {
        user.setRoles(roleService.getUserListRole(rol));
        userService.addNewUser(user);
        return "redirect:/admin/users";
    }

    @PatchMapping(value = "/admin/{id}")
    public String updateUser(@ModelAttribute(value = "user") User user, BindingResult bindingResult, @RequestParam(value = "roles") String[] rol, Model model) {
        System.out.println("1");
        user.setRoles(roleService.getUserListRole(rol));
        System.out.println("2");
        userService.editUser(user);
        System.out.println("updateUser");
        return "redirect:/admin/users";
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
