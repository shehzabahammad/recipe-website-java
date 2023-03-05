package com.shehzab.recipewebsite.service;

import com.shehzab.recipewebsite.entity.Recipe;
import com.shehzab.recipewebsite.entity.User;
import com.shehzab.recipewebsite.model.RecipeRequest;
import com.shehzab.recipewebsite.model.RecipeResponse;
import com.shehzab.recipewebsite.repository.RecipeRepository;
import com.shehzab.recipewebsite.repository.UsersRepository;
import com.shehzab.recipewebsite.securityconfig.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RecipeResponse addRecipe(RecipeRequest recipeRequest, String authToken) {
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
    }

    public List<RecipeResponse> getAllRecipes(String authToken) {
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
    }

    public RecipeResponse getRecipeById(long recipeId, String authToken) throws HttpClientErrorException {
        var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
        var user = usersRepository.findByEmail(username);
        var recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            return RecipeResponse.builder()
                    .recipeId(recipe.get().getId())
                    .title(recipe.get().getTitle())
                    .ingredients(recipe.get().getIngredients())
                    .making(recipe.get().getMaking())
                    .specialNotes(recipe.get().getSpecialNotes())
                    .owner(recipe.get().getUser().getId() == user.getId())
                    .build();
        }
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public RecipeResponse editRecipeById(long recipeId, RecipeRequest recipeRequest, String authToken) throws HttpClientErrorException {
        var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
        var user = usersRepository.findByEmail(username);
        var recipeOwner = recipeRepository.findById(recipeId);
        if (recipeOwner.isPresent()) {
            if (recipeOwner.get().getUser().getId() == user.getId()) {
                var recipe = Recipe.builder()
                        .title(recipeRequest.getTitle())
                        .ingredients(recipeRequest.getIngredients())
                        .making(recipeRequest.getMaking())
                        .specialNotes(recipeRequest.getSpecialNotes())
                        .Id(recipeId)
                        .user(user)
                        .build();
                var resp = recipeRepository.save(recipe);
                return RecipeResponse.builder()
                        .title(resp.getTitle())
                        .ingredients(resp.getIngredients())
                        .making(resp.getMaking())
                        .recipeId(resp.getId())
                        .specialNotes(resp.getSpecialNotes())
                        .build();
            }
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}
