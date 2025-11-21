package com.louter.collab.domain.profile.service;

import com.louter.collab.domain.profile.dto.request.ProfileRequest;
import com.louter.collab.domain.profile.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(Long userId, ProfileRequest profileRequest);
    ProfileResponse getProfile(Long userId);
    ProfileResponse updateProfile(Long userId, ProfileRequest profileRequest);
}
