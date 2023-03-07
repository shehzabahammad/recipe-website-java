package com.shehzab.recipewebsite.service;

import com.shehzab.recipewebsite.entity.User;
import com.shehzab.recipewebsite.model.ProfileRequest;
import com.shehzab.recipewebsite.model.ProfileResponse;
import com.shehzab.recipewebsite.repository.UsersRepository;
import com.shehzab.recipewebsite.securityconfig.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ProfileResponse getProfileById(String id, String authToken) throws HttpClientErrorException {
        var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
        var user = usersRepository.findByEmail(username);

        var profile = usersRepository.findById(id);

        if (profile.isPresent()) {
            if (user.getId().equals(id)) {
                return ProfileResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .isOwner(true)
                        .build();
            }
            return ProfileResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .isOwner(false)
                    .build();
        }

        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public ProfileResponse editProfileById(String id, ProfileRequest profileRequest, String authToken) throws HttpClientErrorException {
        var username = jwtTokenProvider.extractAllClaims(authToken).getSubject();
        var user = usersRepository.findByEmail(username);

        var profile = usersRepository.findById(id);

        if (profile.isPresent()) {
            var prof = profile.get();
            if (user.getId().equals(prof.getId())) {
                prof.setFirstName(profileRequest.getFirstName());
                prof.setLastName(profileRequest.getLastName());
                var updatedProfile = usersRepository.save(prof);
                return ProfileResponse.builder()
                        .firstName(updatedProfile.getFirstName())
                        .lastName(updatedProfile.getLastName())
                        .email(updatedProfile.getEmail())
                        .id(updatedProfile.getId())
                        .isOwner(true)
                        .build();
            }
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}
