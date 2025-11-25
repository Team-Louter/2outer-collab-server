package com.louter.collab.domain.chat.repository;

import com.louter.collab.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_IdOrderByCreatedAtAsc(Long chatRoomId);
}
