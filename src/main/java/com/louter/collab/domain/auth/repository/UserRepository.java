package com.louter.collab.domain.auth.repository;

import com.louter.collab.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 유저 조회
    Optional<User> findByUserId(Long userId);
    Optional<User> findByUserEmail(String email);

    // 유저 존재 확인
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserName(String userName);
}
