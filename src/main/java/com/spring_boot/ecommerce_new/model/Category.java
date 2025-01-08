package com.spring_boot.ecommerce_new.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;

    @NotBlank
    @Size(min = 3,message = "category name must have minimum more than 3 letters. ")
    private String categoryName;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> products;

}
