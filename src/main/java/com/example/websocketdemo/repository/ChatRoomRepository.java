package com.example.websocketdemo.repository;

import com.example.websocketdemo.model.ChatRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatRoomRepository {
    private List<ChatRoom> chatRooms= new ArrayList<>();


    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> result = new ArrayList<>(chatRooms);
        Collections.reverse(result);
        return result;
    }
}
