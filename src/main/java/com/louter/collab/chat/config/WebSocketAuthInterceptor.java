package com.louter.collab.chat.config;

import com.louter.collab.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            // CONNECT 명령어일 때 토큰 검증 및 사용자 인증
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                // Authorization 헤더에서 토큰 추출
                List<String> authorization = accessor.getNativeHeader("Authorization");
                
                if (authorization != null && !authorization.isEmpty()) {
                    String token = authorization.get(0);
                    
                    // Bearer 접두사 제거
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7).trim();
                    }

                    try {
                        // 토큰 검증 및 사용자 ID 추출
                        jwtAuthenticationFilter.validateToken(token);
                        Long userId = jwtAuthenticationFilter.userIdFromToken(token);

                        // Authentication 객체 생성 및 설정
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userId.toString(), 
                                null, 
                                Collections.emptyList()
                        );
                        
                        accessor.setUser(authentication);
                        
                        // 세션 속성에 저장 (SEND 메시지에서 재사용)
                        if (accessor.getSessionAttributes() != null) {
                            accessor.getSessionAttributes().put("userId", userId.toString());
                        }
                        
                        System.out.println("WebSocket user authenticated: " + userId);
                    } catch (Exception e) {
                        System.err.println("WebSocket authentication failed: " + e.getMessage());
                    }
                }
            } 
            // SEND, SUBSCRIBE 등 다른 메시지에서도 사용자 정보 유지
            else if (accessor.getUser() == null && accessor.getSessionAttributes() != null) {
                String userId = (String) accessor.getSessionAttributes().get("userId");
                if (userId != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userId, 
                            null, 
                            Collections.emptyList()
                    );
                    accessor.setUser(authentication);
                }
            }
        }

        return message;
    }
}
