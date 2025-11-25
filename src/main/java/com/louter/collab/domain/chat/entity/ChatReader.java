package com.louter.collab.domain.chat.entity;

import com.louter.collab.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_readers")
@IdClass(ChatReader.ChatReaderId.class)
public class ChatReader {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage chatMessage;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @UpdateTimestamp
    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @Builder
    public ChatReader(ChatMessage chatMessage, User user) {
        this.chatMessage = chatMessage;
        this.user = user;
    }

    public static class ChatReaderId implements Serializable {
        private Long chatMessage;
        private Long user;

        public ChatReaderId() {}

        public ChatReaderId(Long chatMessage, Long user) {
            this.chatMessage = chatMessage;
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChatReaderId that = (ChatReaderId) o;
            if (!chatMessage.equals(that.chatMessage)) return false;
            return user.equals(that.user);
        }

        @Override
        public int hashCode() {
            int result = chatMessage.hashCode();
            result = 31 * result + user.hashCode();
            return result;
        }
    }
}
