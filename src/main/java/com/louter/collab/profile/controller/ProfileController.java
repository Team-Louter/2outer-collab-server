package com.louter.collab.profile.controller;

import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.profile.repository.ProfileRepository;
import com.louter.collab.team.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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
