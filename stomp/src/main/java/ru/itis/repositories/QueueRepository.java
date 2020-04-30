package ru.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.models.Queue;

import java.util.Optional;


public interface QueueRepository extends JpaRepository<Queue, Long> {

    @Query(value = "select queue from Queue queue where queue.name = :name")
    Optional<Queue> findByName(String name);
}
