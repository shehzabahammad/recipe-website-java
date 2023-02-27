package com.shehzab.recipewebsite.service;

import com.shehzab.recipewebsite.entity.Recipe;
import com.shehzab.recipewebsite.entity.User;
import com.shehzab.recipewebsite.model.RecipeRequest;
import com.shehzab.recipewebsite.model.RecipeResponse;
import com.shehzab.recipewebsite.repository.RecipeRepository;
import com.shehzab.recipewebsite.repository.UsersRepository;
import com.shehzab.recipewebsite.securityconfig.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RecipeResponse addRecipe(RecipeRequest recipeRequest, String authToken) throws Exception {
        try {
            var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
            var user = usersRepository.findByEmail(username);
            var recipeBuilder = Recipe.builder()
                    .title(recipeRequest.getTitle())
                    .ingredients(recipeRequest.getIngredients())
                    .making(recipeRequest.getMaking())
                    .specialNotes(recipeRequest.getSpecialNotes())
                    .user(user)
                    .build();
            var recipe = recipeRepository.save(recipeBuilder);
            return RecipeResponse.builder()
                    .recipeId(recipe.getId())
                    .title(recipe.getTitle())
                    .ingredients(recipe.getIngredients())
                    .making(recipe.getMaking())
                    .specialNotes(recipe.getSpecialNotes())
                    .owner(true)
                    .build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<RecipeResponse> getAllRecipes(String authToken) throws Exception {
        try {
            var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
            var user = usersRepository.findByEmail(username);
            var responseList = new ArrayList<RecipeResponse>();
            var list = recipeRepository.findAll();
            for (Recipe recipe : list) {
                responseList.add(RecipeResponse.builder()
                        .recipeId(recipe.getId())
                        .title(recipe.getTitle())
                        .ingredients(recipe.getIngredients())
                        .making(recipe.getMaking())
                        .specialNotes(recipe.getSpecialNotes())
                        .owner(recipe.getUser().getId() == user.getId())
                        .build());
            }
            return responseList;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public RecipeResponse getRecipeById(String authToken) throws Exception {
        try{

        }catch (Exception e){
            throw new Exception(e);
        }
    }


}
