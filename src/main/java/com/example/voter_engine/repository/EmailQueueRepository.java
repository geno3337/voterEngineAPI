package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.emailQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailQueueRepository extends JpaRepository<emailQueue,Integer> {

    emailQueue findByStatus(String pending);

    emailQueue findFirstByStatusOrderByQueuedAtAsc(String status);
}
