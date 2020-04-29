package ru.itis.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.itis.models.Queue;
import ru.itis.repositories.QueueRepository;
import ru.itis.services.QueueService;
import ru.itis.services.SubscriptionService;
import ru.itis.services.WebSocketSessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Map<Queue, WebSocketSession> SUBSCRIBERS = new HashMap<>();

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private WebSocketSessionService webSocketSessionService;

    @Autowired
    private QueueService queueService;

    @Override
    public Queue subscribe(WebSocketSession session, String queueName) {
        Optional<Queue> queueOptional = queueRepository.findByName(queueName);
        Queue queue = queueOptional.orElseGet(() -> queueRepository.save(Queue.builder()
                .name(queueName).build()));
        if (webSocketSessionService.hasQueueInSession(session)) {
            throw new IllegalStateException("Already subscribed!");
        }
        if (!SUBSCRIBERS.containsKey(queue)) {
            webSocketSessionService.addQueueToSession(session, queue);
            SUBSCRIBERS.put(queue, session);
            return queue;
        }
        throw new IllegalStateException("Queue is already used");
    }

    @Override
    public void unsubscribe(WebSocketSession session) {
        if (webSocketSessionService.hasQueueInSession(session)) {
            SUBSCRIBERS.remove(webSocketSessionService.getQueueFromSession(session));
        }
    }

    @Override
    public WebSocketSession getSubscriberFromQueue(Queue queue) {
        WebSocketSession socketSession = SUBSCRIBERS.get(queue);
        if (socketSession != null) {
            return socketSession;
        }
        throw new IllegalArgumentException("No subscribers for this session");
    }
}
