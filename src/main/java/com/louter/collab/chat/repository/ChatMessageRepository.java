package com.louter.collab.chat.repository;

import com.louter.collab.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_IdOrderByCreatedAtAsc(Long chatRoomId);
}
