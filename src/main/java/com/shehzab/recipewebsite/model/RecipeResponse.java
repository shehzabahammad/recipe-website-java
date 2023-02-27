package com.shehzab.recipewebsite.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {

    private long recipeId;
    private String title;
    private String ingredients;
    private String making;
    private String specialNotes;
    private boolean owner;
}
