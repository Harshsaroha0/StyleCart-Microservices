package com.stylecart.catalogservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
public class Category extends BaseEntity {

    @Column(nullable = false , unique = true,length = 100)
    private String name;

    @Column(length = 500)
    private String description;

}
