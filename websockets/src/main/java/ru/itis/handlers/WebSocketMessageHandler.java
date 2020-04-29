package ru.itis.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.models.Queue;
import ru.itis.models.Status;
import ru.itis.services.MessageService;
import ru.itis.services.SubscriptionService;
import ru.itis.services.WebSocketSessionService;

@Component
@Slf4j
public class WebSocketMessageHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private WebSocketSessionService sessionService;

    @Autowired
    private MessageService messageService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text = message.getPayload();
        JsonNode root = objectMapper.readTree(text);
        String command = root.get("command").asText();
        if (command.equals("send")) {
            messageService.send(root.get("body").toString(),
                    sessionService.getQueueFromSession(session));
        } else if (command.equals("subscribe")) {
            Queue queue = subscriptionService.subscribe(session, root.get("queueName").asText());
            messageService.sendAllNotCompletedMessagesForQueue(queue);
        } else if (command.equals("accepted")) {
            messageService.changeStatus(root.get("messageId").asText(), Status.ACCEPTED);
        } else if (command.equals("completed")) {
            log.info("Command completed called");
            messageService.changeStatus(root.get("messageId").asText(), Status.COMPLETED);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        subscriptionService.unsubscribe(session);
    }
}
