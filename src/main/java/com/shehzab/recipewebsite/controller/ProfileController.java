package com.shehzab.recipewebsite.controller;

import com.shehzab.recipewebsite.constant.Constants;
import com.shehzab.recipewebsite.model.ProfileRequest;
import com.shehzab.recipewebsite.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{profileId}")
    public ResponseEntity<?> getProfileById(@PathVariable String profileId, HttpServletRequest httpServletRequest) {
        try {
            var authToken = httpServletRequest.getHeader(Constants.AUTHORIZATION).substring(7);
            return ResponseEntity.ok(profileService.getProfileById(profileId, authToken));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<?> editProfileById(@PathVariable String profileId, @RequestBody ProfileRequest profileRequest, HttpServletRequest httpServletRequest) {
        try {
            var authToken = httpServletRequest.getHeader(Constants.AUTHORIZATION).substring(7);
            return ResponseEntity.ok(profileService.editProfileById(profileId, profileRequest, authToken));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
