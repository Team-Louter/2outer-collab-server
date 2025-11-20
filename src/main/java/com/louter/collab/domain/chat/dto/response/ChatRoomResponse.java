package com.louter.collab.domain.chat.dto.response;

import com.louter.collab.domain.chat.entity.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private final Long chatRoomId;
    private final String channelName;
    private final Long teamId;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.channelName = chatRoom.getChannelName();
        this.teamId = chatRoom.getTeam().getTeamId();
    }
}
