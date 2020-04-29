package ru.itis.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import ru.itis.models.Message;
import ru.itis.models.Queue;
import ru.itis.models.Status;
import ru.itis.repositories.MessageRepository;
import ru.itis.services.MessageService;
import ru.itis.services.SubscriptionService;
import ru.itis.services.WebSocketSessionService;

import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriptionService subscriptionService;

    @SneakyThrows
    @Override
    public void send(String messageBodyText, Queue queue) {
        Message message = Message.builder()
                .text(messageBodyText)
                .messageId(UUID.randomUUID().toString())
                .queue(queue)
                .status(Status.RECEIVED)
                .build();
        send(message, queue);
    }

    private TextMessage generateReceiveTextMessage(Message message, Queue queue) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("command", "receive");
        root.put("messageId", message.getMessageId());
        root.put("body", message.getText());
        log.info(root.toString());
        return new TextMessage(root.toString());
    }

    @Override
    public void changeStatus(String messageId, Status status) {
        Message message = messageRepository.findMessageByMessageId(messageId).orElseThrow(IllegalArgumentException::new);
        message.setStatus(status);
        messageRepository.save(message);
    }

    @Override
    public void sendAllNotCompletedMessagesForQueue(Queue queue) {
        messageRepository.findAllByStatusNotAndQueue(Status.COMPLETED, queue)
                .forEach(message -> send(message, queue));
    }

    @SneakyThrows
    private void send(Message message, Queue queue) {
        messageRepository.save(message);
        subscriptionService.getSubscriberFromQueue(queue).sendMessage(generateReceiveTextMessage(message, queue));

    }
}
