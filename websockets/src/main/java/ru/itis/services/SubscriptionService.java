package ru.itis.services;

import org.springframework.web.socket.WebSocketSession;
import ru.itis.models.Queue;

public interface SubscriptionService {

    Queue subscribe(WebSocketSession session, String queueName);

    void unsubscribe(WebSocketSession session);

    WebSocketSession getSubscriberFromQueue(Queue queue);

}
