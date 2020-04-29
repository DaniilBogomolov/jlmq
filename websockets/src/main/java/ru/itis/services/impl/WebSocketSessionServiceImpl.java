package ru.itis.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.itis.exceptions.NoQueueException;
import ru.itis.models.Queue;
import ru.itis.services.WebSocketSessionService;

import java.util.NoSuchElementException;


@Service
public class WebSocketSessionServiceImpl implements WebSocketSessionService {

    public static final String QUEUE = "queue";

    @Override
    public void addQueueToSession(WebSocketSession session, Queue queue) {
        session.getAttributes().put(QUEUE, queue);
    }

    @Override
    public boolean hasQueueInSession(WebSocketSession session) {
        return session.getAttributes().containsKey(QUEUE);
    }

    @Override
    public Queue getQueueFromSession(WebSocketSession session) {
        Object queue = session.getAttributes().get(QUEUE);
        if (queue != null) {
            return (Queue) queue;
        }
        throw new NoQueueException("No queue in session!");
    }
}
