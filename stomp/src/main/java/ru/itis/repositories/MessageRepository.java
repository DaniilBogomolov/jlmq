package ru.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.models.Message;
import ru.itis.models.Queue;
import ru.itis.models.Status;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findMessageByMessageId(String messageId);

    List<Message> findAllByStatusNotAndQueue(Status status, Queue queue);
}
