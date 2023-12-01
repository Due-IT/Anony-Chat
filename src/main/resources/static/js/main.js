'use strict';


var chatRoomPage = document.querySelector('#chatroom-page');
var chatRoomList = document.querySelector('#chatroom-list');
var chatRoomForm = document.querySelector('#chatroom-name-form');
var roomnameInput = document.querySelector('#roomname');
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var exitButton = document.querySelector('#exit-chat-room');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];



function introPage(event) {
    //데이터 받아오기
    var url = "http://localhost:8080/chatrooms";
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);

            data.forEach(item => {
                var chatRoomElement = document.createElement('li');
                chatRoomElement.classList.add('chatroom');
                chatRoomElement.setAttribute('roomId', item.roomId);
                chatRoomElement.setAttribute('onclick', 'chatRoomClick(this)');

                var roomnameElement = document.createElement('span');
                var roonameText = document.createTextNode(item.roomName);
                roomnameElement.appendChild(roonameText);

                var userCountElement = document.createElement('p');
                var userCountText = document.createTextNode('현재 인원 : '+item.userCount+'명');
                userCountElement.appendChild(userCountText);

                chatRoomElement.appendChild(roomnameElement);
                chatRoomElement.appendChild(userCountElement);

                chatRoomList.appendChild(chatRoomElement);

                chatRoomPage.scrollTop=chatRoomPage.scrollHeight;
            });
        });
};

function chatRoomClick(room){
    chatRoomPage.classList.add('hidden');
    usernamePage.classList.remove('hidden');

    var roomId = room.getAttribute('roomId');

    var url = "http://localhost:8080/roomname?roomId="+roomId;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            localStorage.setItem('roomName', data);
        });

    localStorage.setItem('roomId', roomId);
}

function createChatRoom(event){
    var nameText = roomnameInput.value.trim();

    var url = "http://localhost:8080/chatroom"
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type' : 'application/json'
        },
        body: nameText
    })
        .then(response =>{
            return response.json();
        })
        .then(data => {
            chatRoomPage.classList.add('hidden');
            usernamePage.classList.remove('hidden');

            localStorage.setItem('roomId', data.roomId);
            localStorage.setItem('roomName', data.roomName);
        });
    event.preventDefault(); //페이지가 다시 로드 되는 것을 막는다.
}

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    //localstorage에서 아이템 꺼내기
    var roomId = localStorage.getItem('roomId');
    var roomName = localStorage.getItem('roomName');

    var roomnameElement = document.querySelector('#chat-page .chat-header h2');
    roomnameElement.innerText = roomName;


    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public/'+roomId, onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN', roomId: roomId})
    )

    //GET으로 roomName들고 오기

    connectingElement.classList.add('hidden');
}

function exitChatRoom(event){
    stompClient.unsubscribe();
    localStorage.removeItem('roomId');

    chatPage.classList.add('hidden');
    chatRoomPage.classList.remove('hidden');

    location.reload();
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    var roomId = localStorage.getItem('roomId');

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            roomId: roomId
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}


document.addEventListener("DOMContentLoaded", introPage,true);
chatRoomForm.addEventListener('submit', createChatRoom, true)
usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
exitButton.addEventListener('click', exitChatRoom, true);