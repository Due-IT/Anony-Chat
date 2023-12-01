package com.example.websocketdemo.repository;

import com.example.websocketdemo.model.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Repository
public class ChatRoomRepository {
    private static List<ChatRoom> chatRooms= new ArrayList<>();

    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> result = new ArrayList<>(chatRooms);
        Collections.reverse(result);
        return result;
    }

    public static ChatRoom getChatRoom(String roomId) {
        return chatRooms.stream()
                .filter(room -> roomId.equals(room.getRoomId()))
                .findFirst()
                .orElse(null);
    }
    public static void plusUserCnt(ChatRoom chatRoom) {
        chatRoom.setUserCount(chatRoom.getUserCount()+1);
    }

    public String addUser(String roomId, String sender) {
        ChatRoom chatRoom = getChatRoom(roomId);
        plusUserCnt(chatRoom);
        chatRoom.getUsers().add("sender");
        return sender;
    }

    public void deleteUser(String username, String roomId) {
        List<String> chatRoomUsers = getChatRoom(roomId).getUsers();

        Iterator<String> iterator = chatRoomUsers.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name.equals(username)) {
                iterator.remove();
            }
        }
    }

    public ChatRoom createChatRoom(String roomName){
        ChatRoom chatRoom = ChatRoom.create(roomName);
        chatRooms.add(chatRoom);
        return chatRoom;
    }
}
