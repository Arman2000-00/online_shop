package com.example.onlineshop.controller;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/deleteCart")
    public String deleteProduct(@RequestParam("id") int id) {
        Optional<Cart> byId = cartRepository.findById(id);
        if (byId.isPresent()) {
            cartRepository.deleteById(id);
            return "redirect:/cart";
        }
        return "redirect:/admin";
    }

    @GetMapping("/cart")
    public String cart(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser() != null) {
            Optional<User> byId = userRepository.findById(springUser.getUser().getId());
            if (byId.isPresent()) {
                List<Cart> allByUserId = cartRepository.findAllByUserId(byId.get().getId());
                modelMap.addAttribute("user", springUser.getUser());
                modelMap.addAttribute("cart", allByUserId);
                return "cart";
            }

        }
        return "redirect:/";
    }

    @GetMapping("/cartAdd")
    public String cartView(@RequestParam("id") int id, @AuthenticationPrincipal SpringUser springUser, ModelMap map) {
        if (springUser != null && springUser.getUser() != null) {
            Optional<User> byId = userRepository.findById(springUser.getUser().getId());
            Optional<Product> productById = productRepository.findById(id);
            {
                if (productById.isPresent()) {
                    if (byId.isPresent()) {
                        Cart cart = new Cart();
                        cart.setUser(springUser.getUser());
                        cart.setProduct(productById.get());
                        cartRepository.save(cart);
                        return "redirect:/shop";
                    }
                }
            }
        }
        map.addAttribute("message", "login in for buying!");
        return "redirect:/";
    }
}
