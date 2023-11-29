package com.example.websocketdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ChatRooms {
    List<ChatRoom> chatRooms;

    public void addChatRoom(ChatRoom chatRoom){
        chatRooms.add(chatRoom);
    }
}
