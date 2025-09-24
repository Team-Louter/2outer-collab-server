package com.louter.collab.auth.repository;

import com.louter.collab.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 유저 조회
    Optional<User> findByUserEmail(String userEmail);

    // 유저 존재 확인
    boolean existsByUserEmail(String userEmail);
}
