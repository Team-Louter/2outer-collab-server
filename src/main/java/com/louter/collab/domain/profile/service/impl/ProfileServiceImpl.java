package com.louter.collab.domain.profile.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.profile.dto.request.ProfileRequest;
import com.louter.collab.domain.profile.dto.response.ProfileResponse;
import com.louter.collab.domain.profile.dto.response.ProjectInfoResponse;
import com.louter.collab.domain.profile.entity.Profile;
import com.louter.collab.domain.profile.repository.ProfileRepository;
import com.louter.collab.domain.profile.service.ProfileService;
import com.louter.collab.domain.team.repository.UserTeamRepository;
import com.louter.collab.global.common.exception.AlreadyExistProfileException;
import com.louter.collab.global.common.exception.ProfileNotFoundException;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    // 프로필 생성
    @Transactional
    @Override
    public ProfileResponse createProfile(Long userId, ProfileRequest profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        if (profileRepository.existsById(userId)) {
            throw new AlreadyExistProfileException("이미 프로필이 존재합니다.");
        }

        user.setUserName(profileRequest.getUserName());
        Profile profile = Profile.builder()
                .userId(userId)
                .user(user)
                .profileImageUrl(profileRequest.getProfileImageUrl() == null ? "http://api.teamcollab.site/api/files/download/02c775b8-2792-44f2-955c-9c08d0ec4d10_defaultProfileImage.png" : profileRequest.getProfileImageUrl())
                .bio(profileRequest.getBio() == null ? "안녕하세요!!" : profileRequest.getBio())
                .build();

        Profile savedProfile = profileRepository.saveAndFlush(profile);

        List<ProjectInfoResponse> projects = userTeamRepository.findByUser_UserId(userId).stream()
                .map(userTeam -> ProjectInfoResponse.builder()
                        .teamId(userTeam.getTeam().getTeamId())
                        .projectPicture(userTeam.getTeam().getProfilePicture())
                        .build())
                .collect(Collectors.toList());

        return ProfileResponse.of(savedProfile, user.getUserName(), projects);
    }

    // 프로필 조회
    @Override
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("유저를 찾을 수 없습니다."));

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException("프로필을 찾을 수 없습니다."));

        List<ProjectInfoResponse> projects = userTeamRepository.findByUser_UserId(userId).stream()
                .map(userTeam -> ProjectInfoResponse.builder()
                        .teamId(userTeam.getTeam().getTeamId())
                        .projectPicture(userTeam.getTeam().getProfilePicture())
                        .build())
                .collect(Collectors.toList());
        return ProfileResponse.of(profile, user.getUserName(), projects);
    }

    // 프로필 수정
    @Transactional
    @Override
    public ProfileResponse updateProfile(Long userId, ProfileRequest profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("유저를 찾을 수 없습니다."));

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException("프로필을 찾을 수 없습니다."));

        user.setUserName(profileRequest.getUserName());
        profile.setProfileImageUrl(profileRequest.getProfileImageUrl() == null ? "http://api.teamcollab.site/api/files/download/02c775b8-2792-44f2-955c-9c08d0ec4d10_defaultProfileImage.png" : profileRequest.getProfileImageUrl());
        profile.setBio(profileRequest.getBio());

        List<ProjectInfoResponse> projects = userTeamRepository.findByUser_UserId(userId).stream()
                .map(userTeam -> ProjectInfoResponse.builder()
                        .teamId(userTeam.getTeam().getTeamId())
                        .projectPicture(userTeam.getTeam().getProfilePicture())
                        .build())
                .collect(Collectors.toList());
        return ProfileResponse.of(profile, user.getUserName(), projects);
    }
}
