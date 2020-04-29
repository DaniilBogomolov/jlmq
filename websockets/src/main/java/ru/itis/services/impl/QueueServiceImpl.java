package ru.itis.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.dto.QueueDto;
import ru.itis.models.Queue;
import ru.itis.repositories.QueueRepository;
import ru.itis.services.QueueService;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;


    @Override
    public void create(QueueDto queueDto) {
        Queue queue = Queue.builder()
                .name(queueDto.getQueueName())
                .build();
        queueRepository.save(queue);
    }
}
