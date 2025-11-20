package com.louter.collab.domain.profile.controller;

import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.profile.repository.ProfileRepository;
import com.louter.collab.domain.team.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    // 프로필 생성
}
