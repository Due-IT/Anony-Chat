## SpringBoot WebSocket Chatroom Application

### 소개

스프링을 사용해 구현한 웹소켓 채팅 앱입니다.


### 기능

여러개의 채팅방을 확인하고, 매 채팅방마다 닉네임을 설정하여 채팅에 참여할 수 있습니다!


### 환경

1. Java - 11

2. Maven - 4.0.0.xsd

3. Spring boot - 2.5.5
   

### 실행방법

**1. Clone the application**

```gradle
git clone https://github.com/FhRh/Spring_Socket_Chat.git
```

**2. Build and run the app using maven**

```gradle
cd spring-boot-websocket-chat-demo
mvn package
java -jar target/websocket-demo-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run the app directly without packaging it like so -

```gradle
mvn spring-boot:run
```


### 참고 사항

만약 채팅방을 추가적으로 구현한 앱을 확인하고 싶다면 feature/AddChatroom 브랜치를 확인해주세요!


### 원본 출처 

```gradle
https://github.com/callicoder/spring-boot-websocket-chat-demo
```

추가적으로 구현된 기능은 feature/AddChatroom을 확인해주세요
대략 500줄 가량의 추가/수정 사항이 있습니다.
