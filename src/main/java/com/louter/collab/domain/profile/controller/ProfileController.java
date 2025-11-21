package com.louter.collab.domain.profile.controller;

import com.louter.collab.domain.profile.dto.request.ProfileRequest;
import com.louter.collab.domain.profile.dto.response.ProfileResponse;
import com.louter.collab.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    // 프로필 생성
    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponse> createProfile(@PathVariable("userId") Long userId, @RequestBody ProfileRequest profileRequest) {
        ProfileResponse profileResponse = profileService.createProfile(userId, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
    }
}
