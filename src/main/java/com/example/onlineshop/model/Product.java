package com.example.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double price;
    @Column
    @Enumerated(EnumType.STRING)
    private Size size;
    @Column
    private int count;
    @Column(name = "pic_url")
    private String picUrl;
    @Column
    private Date date;
    @Column(name = "clothes_type")
    @Enumerated(EnumType.STRING)
    private ClothesType clothesType;
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    @Enumerated(EnumType.STRING)
    private Shipping shipping;
    @Column
    private String color;

}
