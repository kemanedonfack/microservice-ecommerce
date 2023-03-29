package com.kemane.microservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name= "customer_name")
    private String clientName;

    @Column(name= "product_name")
    private String productName;

    @Column(name= "product_quantity")
    private int productQuantity;

    @Column(name= "unit_price")
    private int unitPrice;

    @Column(name= "total")
    private double total;
}
