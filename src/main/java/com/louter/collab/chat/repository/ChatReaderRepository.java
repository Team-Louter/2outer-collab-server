package com.louter.collab.chat.repository;

import com.louter.collab.chat.domain.ChatReader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReaderRepository extends JpaRepository<ChatReader, ChatReader.ChatReaderId> {
}
