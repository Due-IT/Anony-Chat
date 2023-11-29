package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.model.ChatRoom;
import com.example.websocketdemo.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {
    private ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        String destination = "/topic/public";


        return chatMessage;
    } //메시지를 구독한 주제로 전송합니다.

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        chatRoomRepository.plusUserCnt(chatMessage.getRoomId());

        // 채팅방에 유저 추가 및 UserUUID 반환
        String username = chatRoomRepository.addUser(chatMessage.getRoomId(), chatMessage.getSender());

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        return chatMessage;
    }

    @GetMapping("/chatrooms")
    public List<ChatRoom> getChatRoomList(){
        List<ChatRoom> chatRooms = chatRoomRepository.getChatRoomList();
        return chatRooms;
    }

}
