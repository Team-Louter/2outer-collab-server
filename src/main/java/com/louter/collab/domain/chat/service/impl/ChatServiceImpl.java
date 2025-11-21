package com.louter.collab.domain.chat.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.chat.entity.ChatMessage;
import com.louter.collab.domain.chat.entity.ChatMessageFile;
import com.louter.collab.domain.chat.entity.ChatReader;
import com.louter.collab.domain.chat.entity.ChatRoom;
import com.louter.collab.domain.chat.dto.request.ChatMessageRequest;
import com.louter.collab.domain.chat.dto.response.ChatMessageResponse;
import com.louter.collab.domain.chat.dto.response.ChatRoomResponse;
import com.louter.collab.domain.chat.repository.ChatMessageFileRepository;
import com.louter.collab.domain.chat.repository.ChatMessageRepository;
import com.louter.collab.domain.chat.repository.ChatReaderRepository;
import com.louter.collab.domain.chat.repository.ChatRoomRepository;
import com.louter.collab.domain.chat.service.ChatService;
import com.louter.collab.global.common.exception.TeamNotFoundException;
import com.louter.collab.domain.role.entity.Permission;
import com.louter.collab.domain.role.service.RoleService;
import com.louter.collab.domain.team.entity.Team;
import com.louter.collab.domain.team.repository.TeamRepository;
import com.louter.collab.domain.team.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageFileRepository chatMessageFileRepository;
    private final ChatReaderRepository chatReaderRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponse saveMessage(Long chatRoomId, Long senderId, ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        // 사용자가 해당 팀의 멤버인지 확인
        Long teamId = chatRoom.getTeam().getTeamId();
        if (!userTeamRepository.existsByUser_UserIdAndTeam_TeamId(senderId, teamId)) {
            throw new IllegalArgumentException("You are not a member of this team");
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(request.getMessage())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

    if (request.getFileUrls() != null && !request.getFileUrls().isEmpty()) {
        List<ChatMessageFile> files = request.getFileUrls().stream()
            .map(fileUrl -> ChatMessageFile.builder()
                .chatMessage(savedMessage)
                .fileUrl(fileUrl)
                .build())
            .collect(Collectors.toList());
        chatMessageFileRepository.saveAll(files);
    }

        return ChatMessageResponse.builder()
                .messageId(savedMessage.getId())
                .senderId(savedMessage.getSender().getUserId())
                .senderName(savedMessage.getSender().getUserName())
                .message(savedMessage.getMessage())
                .createdAt(savedMessage.getCreatedAt())
                .fileUrls(request.getFileUrls())
                .build();
    }

    @Override
    @Transactional
    public void markMessageAsRead(Long chatRoomId, Long messageId, Long userId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!chatMessage.getChatRoom().getId().equals(chatRoomId)) {
            throw new IllegalArgumentException("Message does not belong to the specified chat room");
        }

        // 사용자가 해당 팀의 멤버인지 확인
        Long teamId = chatMessage.getChatRoom().getTeam().getTeamId();
        if (!userTeamRepository.existsByUser_UserIdAndTeam_TeamId(userId, teamId)) {
            throw new IllegalArgumentException("You are not a member of this team");
        }

        ChatReader.ChatReaderId chatReaderId = new ChatReader.ChatReaderId(messageId, user.getUserId());
        if (!chatReaderRepository.existsById(chatReaderId)) {
            ChatReader chatReader = ChatReader.builder()
                    .chatMessage(chatMessage)
                    .user(user)
                    .build();
            chatReaderRepository.save(chatReader);
        }
    }

    @Override
    @Transactional
    public ChatRoom createChatRoom(Long teamId, String channelName, User creator) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // 채널 생성 권한 확인 (MANAGE_CHANNELS 퍼미션 필요)
        if (!roleService.hasPermission(creator.getUserId(), teamId, Permission.MANAGE_CHANNELS)) {
            throw new IllegalArgumentException("You do not have permission to create a channel.");
        }

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .team(team)
                .channelName(channelName)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoomResponse> getChatRoomsByTeam(Long teamId) {
        // 팀 존재 확인
        teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        
        List<ChatRoom> chatRooms = chatRoomRepository.findByTeam_TeamId(teamId);
        return chatRooms.stream()
                .map(ChatRoomResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageResponse> getChatMessages(Long teamId, Long roomId, Long userId) {
        // 채팅방 존재 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        // 채팅방이 해당 팀에 속하는지 확인
        if (!chatRoom.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Chat room does not belong to the specified team");
        }

        // 사용자가 해당 팀의 멤버인지 확인
        if (!userTeamRepository.existsByUser_UserIdAndTeam_TeamId(userId, teamId)) {
            throw new IllegalArgumentException("You are not a member of this team");
        }

        // 채팅 메시지 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId);
        
        return messages.stream()
                .map(message -> {
                    List<String> fileUrls = chatMessageFileRepository.findByChatMessage_Id(message.getId())
                            .stream()
                            .map(ChatMessageFile::getFileUrl)
                            .collect(Collectors.toList());
                    
                    return ChatMessageResponse.builder()
                            .messageId(message.getId())
                            .senderId(message.getSender().getUserId())
                            .senderName(message.getSender().getUserName())
                            .message(message.getMessage())
                            .createdAt(message.getCreatedAt())
                            .fileUrls(fileUrls)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
