package ru.itis.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import ru.itis.models.Status;
import ru.itis.services.MessageService;
import ru.itis.services.QueueService;

import java.util.List;

import static java.util.Collections.EMPTY_LIST;


@Component
public class StompMessageHandler implements MessageHandler {

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private QueueService queueService;

    @Autowired
    private MessageService messageService;

    @SneakyThrows
    @Override
    public List<TextMessage> handle(String message, String queueName) {
        JsonNode root = mapper.readTree(message);
        String command = root.get("command").asText();
        if (command.equals("send")) {
            return messageService.send(root.get("body").toString(),
                    queueService.getQueueFromName(queueName));
        } else if (command.equals("subscribe")) {
            return messageService.getAllNotCompletedMessagesForQueue(queueService.getQueueFromName(queueName));
        } else if (command.equals("accepted")) {
            messageService.changeStatus(root.get("messageId").asText(), Status.ACCEPTED);
        } else if (command.equals("completed")) {
            messageService.changeStatus(root.get("messageId").asText(), Status.COMPLETED);
        }
        return EMPTY_LIST;
    }
}
