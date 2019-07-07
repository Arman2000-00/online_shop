package com.example.onlineshop.controller;

import com.example.onlineshop.model.Message;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute Message message) {
        messageRepository.save(message);
        return "redirect:/about";
    }

    @GetMapping("/deleteMessage")
    public String deleteMessage(@RequestParam("id") int id) {
        Optional<Message> byId = messageRepository.findById(id);
        if (byId.isPresent()) {
            messageRepository.deleteById(id);
        }
        return "redirect:/admin";
    }
}

