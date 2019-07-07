package com.example.onlineshop.controller;

import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.*;
import com.example.onlineshop.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginView(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getUserType() == UserType.ADMIN) {
            return "redirect:/admin";
        } else if (springUser.getUser().getUserType() == UserType.USER) {
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerView(ModelMap modelMap) {
        modelMap.addAttribute("countries", countryRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @ModelAttribute Address address) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        addressRepository.save(address);
        user.setAddress(address);
        user.setUserType(UserType.USER);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String admin(ModelMap modelMap) {
        modelMap.addAttribute("products", productRepository.findAll());
        modelMap.addAttribute("sizes", Size.values());
        modelMap.addAttribute("clothesTypes", ClothesType.values());
        modelMap.addAttribute("categories", Category.values());
        modelMap.addAttribute("shipping", Shipping.values());
        modelMap.addAttribute("allMessages", messageRepository.findAll());
        return "admin";
    }

    @GetMapping("/user")
    public String user(ModelMap modelMap) {
        modelMap.addAttribute("products", productRepository.findAll());
        return "user";
    }

}