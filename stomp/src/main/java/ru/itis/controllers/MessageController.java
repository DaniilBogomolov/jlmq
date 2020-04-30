package ru.itis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import ru.itis.handlers.MessageHandler;

import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageHandler messageHandler;


    @MessageMapping("/{queueName}")
    @SendTo("/topic/{queueName}")
    public List<TextMessage> sendMessage(@DestinationVariable String queueName,
                                         String message) {
        return messageHandler.handle(message, queueName);
    }

}
