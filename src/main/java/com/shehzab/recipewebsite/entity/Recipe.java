package com.shehzab.recipewebsite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;
    @Column(name = "title")
    private String title;
    @Column(name = "ingredients")
    private String ingredients;
    @Column(name = "making")
    private String making;
    @Column(name = "special_notes")
    private String specialNotes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
