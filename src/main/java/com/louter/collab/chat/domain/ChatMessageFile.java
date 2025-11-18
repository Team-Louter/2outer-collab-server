package com.louter.collab.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message_files")
public class ChatMessageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage chatMessage;

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    @Builder
    public ChatMessageFile(ChatMessage chatMessage, String fileUrl) {
        this.chatMessage = chatMessage;
        this.fileUrl = fileUrl;
    }
}
