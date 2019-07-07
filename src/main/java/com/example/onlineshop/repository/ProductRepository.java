package com.example.onlineshop.repository;

import com.example.onlineshop.model.Category;
import com.example.onlineshop.model.ClothesType;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop4ByOrderByIdDesc();

    List<Product> findTop8ByOrderByIdDesc();

    Page<Product> findAllByClothesTypeAndCategory(ClothesType clothesType, Category category, Pageable pageable);

    Page<Product> findAllByPriceIsBetween(double price, double price2, Pageable pageable);

    Page<Product> findAllProductsBySize(Size size, Pageable pageable);

    Page<Product> findAllByColor(String color, Pageable pageable);

    Page<Product> findAllByNameContains(String name, Pageable pageable);

    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
}
