package com.louter.collab.domain.chat.repository;

import com.louter.collab.domain.chat.entity.ChatReader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReaderRepository extends JpaRepository<ChatReader, ChatReader.ChatReaderId> {
}
