package ru.itis.services;

import ru.itis.models.Queue;

public interface QueueService {


    Queue getQueueFromName(String name);
}
