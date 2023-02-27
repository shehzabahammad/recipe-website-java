package com.shehzab.recipewebsite.controller;

import com.shehzab.recipewebsite.constant.Constants;
import com.shehzab.recipewebsite.model.RecipeRequest;
import com.shehzab.recipewebsite.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> addRecipe(@RequestBody RecipeRequest recipeRequest, HttpServletRequest httpServletRequest) {
        try {
            var authToken = httpServletRequest.getHeader(Constants.AUTHORIZATION).substring(7);
            return ResponseEntity.ok(recipeService.addRecipe(recipeRequest, authToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipes(HttpServletRequest httpServletRequest) {
        try {
            var authToken = httpServletRequest.getHeader(Constants.AUTHORIZATION).substring(7);
            return ResponseEntity.ok(recipeService.getAllRecipes(authToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
