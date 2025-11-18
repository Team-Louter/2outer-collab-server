package com.louter.collab.chat.repository;

import com.louter.collab.chat.domain.ChatMessageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageFileRepository extends JpaRepository<ChatMessageFile, Long> {
    List<ChatMessageFile> findByChatMessage_Id(Long messageId);
}
