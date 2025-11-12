package com.louter.collab.chat.repository;

import com.louter.collab.chat.domain.ChatMessageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageFileRepository extends JpaRepository<ChatMessageFile, Long> {
}
