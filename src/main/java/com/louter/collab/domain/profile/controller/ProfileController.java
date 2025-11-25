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

    // 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable("userId") Long userId) {
        ProfileResponse profileResponse = profileService.getProfile(userId);
        return ResponseEntity.ok(profileResponse);
    }

    // 프로필 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable("userId") Long userId, @RequestBody ProfileRequest profileRequest) {
        ProfileResponse profileResponse = profileService.updateProfile(userId, profileRequest);
        return ResponseEntity.ok(profileResponse);
    }
}
