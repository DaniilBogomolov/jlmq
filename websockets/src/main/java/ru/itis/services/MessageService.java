package ru.itis.services;

import ru.itis.models.Queue;
import ru.itis.models.Status;

public interface MessageService {

    void send(String messageBodyText, Queue queue);

    void changeStatus(String messageId, Status status);

    void sendAllNotCompletedMessagesForQueue(Queue queue);
}
