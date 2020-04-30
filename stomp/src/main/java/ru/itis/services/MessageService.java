package ru.itis.services;

import org.springframework.web.socket.TextMessage;
import ru.itis.models.Queue;
import ru.itis.models.Status;

import java.util.List;

public interface MessageService {

    List<TextMessage> send(String messageBodyText, Queue queue);

    void changeStatus(String messageId, Status status);

    List<TextMessage> getAllNotCompletedMessagesForQueue(Queue queue);
}
