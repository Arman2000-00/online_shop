package com.example.onlineshop.controller;

import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {
    @Value("${image.upload.dir}")
    String imageUploadDir;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product, @RequestParam(name = "picture") MultipartFile file, @AuthenticationPrincipal SpringUser springUser) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File picture = new File(imageUploadDir + File.separator + fileName);
        file.transferTo(picture);
        product.setPicUrl(fileName);
        product.setDate(new Date());
        productRepository.save(product);
        return "redirect:/admin";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") int id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            productRepository.deleteById(id);
            return "redirect:/admin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/currentProduct")
    public String currentProduct(@RequestParam("id") int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        Optional<Product> byId = productRepository.findById(id);
        modelMap.addAttribute("last4Products", productRepository.findTop4ByOrderByIdDesc());
        if (springUser != null) {
            modelMap.addAttribute("user", springUser.getUser());
        }
        if (byId.isPresent()) {
            modelMap.addAttribute("product", byId.get());
        }
        return "currentProduct";
    }

    @GetMapping("/shop")
    public String shopView(ModelMap modelMap,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @AuthenticationPrincipal SpringUser springUser) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);
        Page<Product> all = productRepository.findAll(PageRequest.of(currentPage - 1, pageSize));
        modelMap.addAttribute("allProducts", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        if (springUser != null) {
            modelMap.addAttribute("user", springUser.getUser());
        }
        return "shop";
    }

    @PostMapping("/searchProduct")
    public String searchProduct(ModelMap modelMap,
                                @RequestParam("search") String name,
                                @RequestParam(value = "page") Optional<Integer> page,
                                @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);
        Page<Product> all = productRepository.findAllByNameContains(name, PageRequest.of(currentPage - 1, pageSize));
        modelMap.addAttribute("allProducts", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        return "shop";
    }

    @GetMapping("/clothesType")
    public String productByCategory(ModelMap modelMap, @RequestParam("name") String name,
                                    @RequestParam("category") String category,
                                    @RequestParam(value = "page") Optional<Integer> page,
                                    @RequestParam(value = "size") Optional<Integer> size) {
        if (name != null && category != null) {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(12);
            ClothesType clothesType = ClothesType.valueOf(name);
            Category categoryType = Category.valueOf(category);
            Page<Product> all = productRepository.findAllByClothesTypeAndCategory(clothesType, categoryType, PageRequest.of(currentPage - 1, pageSize));
            modelMap.addAttribute("allProducts", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
            return "shop";
        }
        return "redirect:/shop";
    }

    @GetMapping("/size")
    public String productBySize(ModelMap modelMap, @RequestParam("clothesSize") String clothesSize,
                                @RequestParam(value = "page") Optional<Integer> page,
                                @RequestParam(value = "size") Optional<Integer> size) {
        if (clothesSize != null) {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(12);
            Size sizeForClothes = Size.valueOf(clothesSize);
            Page<Product> all = productRepository.findAllProductsBySize(sizeForClothes, PageRequest.of(currentPage - 1, pageSize));
            modelMap.addAttribute("allProducts", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
            return "shop";
        }
        return "redirect:/shop";
    }

    @GetMapping("/byColor")
    public String byColor(@RequestParam("color") String color, ModelMap modelMap,
                          @RequestParam(value = "page") Optional<Integer> page,
                          @RequestParam(value = "size") Optional<Integer> size) {
        if (color != null) {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(12);
            Page<Product> all = productRepository.findAllByColor(color, PageRequest.of(currentPage - 1, pageSize));
            modelMap.addAttribute("allProducts", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
        }
        return "shop";
    }

    @PostMapping("/byPrice")
    public String byPrice(ModelMap modelMap, @RequestParam("price") String price, @RequestParam("price2") String
            price2, @RequestParam(value = "page") Optional<Integer> page,
                          @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);
        double priceDb1 = Double.parseDouble(price);
        double price2Dbl = Double.parseDouble(price2);
        Page<Product> all = productRepository.findAllByPriceIsBetween(priceDb1, price2Dbl, PageRequest.of(currentPage - 1, pageSize));
        modelMap.addAttribute("allProducts", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        return "shop";
    }

    @GetMapping("/sortByPrice")
    public String sortByPrice(ModelMap modelMap, @RequestParam("action") String action, @RequestParam(value = "page") Optional<Integer> page,
                              @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);
        if (action.equals("pricePlus")) {

            Page<Product> all = productRepository.findAllByOrderByPriceDesc(PageRequest.of(currentPage - 1, pageSize));
            modelMap.addAttribute("allProducts", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
        } else if (action.equals("priceMinus")) {
            Page<Product> all = productRepository.findAllByOrderByPriceAsc(PageRequest.of(currentPage - 1, pageSize));
            modelMap.addAttribute("allProducts", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
        }
        return "shop";
    }


}