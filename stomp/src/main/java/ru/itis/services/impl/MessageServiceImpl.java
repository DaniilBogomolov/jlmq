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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @SneakyThrows
    @Override
    public List<TextMessage> send(String messageBodyText, Queue queue) {
        Message message = Message.builder()
                .text(messageBodyText)
                .messageId(UUID.randomUUID().toString())
                .queue(queue)
                .status(Status.RECEIVED)
                .build();
        messageRepository.save(message);
        return List.of(generateReceiveTextMessage(message, queue));
    }

    private TextMessage generateReceiveTextMessage(Message message, Queue queue) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("command", "receive");
        root.put("messageId", message.getMessageId());
        root.put("body", message.getText());
        return new TextMessage(root.toString());
    }

    @Override
    public void changeStatus(String messageId, Status status) {
        Message message = messageRepository.findMessageByMessageId(messageId).orElseThrow(IllegalArgumentException::new);
        message.setStatus(status);
        messageRepository.save(message);
    }

    @Override
    public List<TextMessage> getAllNotCompletedMessagesForQueue(Queue queue) {
        return messageRepository.findAllByStatusNotAndQueue(Status.COMPLETED, queue)
                .stream()
                .map(message -> generateReceiveTextMessage(message, queue))
                .collect(Collectors.toList());
    }


}
