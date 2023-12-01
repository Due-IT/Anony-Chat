package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.model.ChatRoom;
import com.example.websocketdemo.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        String destination = "/topic/chat/room/"+chatMessage.getRoomId();

        chatMessage.setContent(chatMessage.getContent());
        messagingTemplate.convertAndSend(destination, chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        String sender = chatRoomRepository.addUser(chatMessage.getRoomId(), chatMessage.getSender());

        headerAccessor.getSessionAttributes().put("username", sender);
        headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

        ChatMessage response = new ChatMessage();
        response.setType(ChatMessage.MessageType.JOIN);
        response.setSender(sender);
        response.setContent(sender + " 님이 입장하셨습니다.");
        response.setRoomId(chatMessage.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), response);

        return response;
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoom>> getChatRoomList(){
        List<ChatRoom> chatRooms = chatRoomRepository.getChatRoomList();

        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    @PostMapping("/chatroom")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody String roomName){
        ChatRoom chatroom = chatRoomRepository.createChatRoom(roomName);
        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }
}
