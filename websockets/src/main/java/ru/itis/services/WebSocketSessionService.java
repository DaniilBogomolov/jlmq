package ru.itis.services;

import org.springframework.web.socket.WebSocketSession;
import ru.itis.models.Queue;

import javax.websocket.Session;
import java.util.Optional;

public interface WebSocketSessionService {

    void addQueueToSession(WebSocketSession session, Queue queue);

    boolean hasQueueInSession(WebSocketSession socketSession);

    Queue getQueueFromSession(WebSocketSession webSocketSession);

}
