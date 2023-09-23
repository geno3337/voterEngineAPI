package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.eventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface eventEntityRepository extends JpaRepository<eventEntity,Integer> {
}
