package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob// used in JPA (Java Persistence API) to designate a field as a large object (LOB). It tells the persistence provider (e.g., Hibernate) to handle the field as a large object in the database.
    private Blob image;
    private String downloadUrl;
    @ManyToOne //many images belong to one product
    @JoinColumn(name = "product_id")
    private Product product;

}

