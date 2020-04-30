package ru.itis.handlers;

import org.springframework.web.socket.TextMessage;
import ru.itis.models.Message;

import java.util.List;

public interface MessageHandler {
    List<TextMessage> handle(String message, String queueName);
}
