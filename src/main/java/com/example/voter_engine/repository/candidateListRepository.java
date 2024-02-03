package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.candidate;
import com.example.voter_engine.Entity.candidateList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface candidateListRepository extends JpaRepository<candidateList,Integer> {
    boolean existsByGmail(String gmail);

    Optional<candidateList> findByGmail(String gmail);

    candidateList findByEmailVerificationToken(String token);

    @Query("SELECT cl FROM candidateList cl WHERE cl.AdminVerified = false")
    Page<candidateList> getCandidate(Pageable Page);

    @Query("SELECT v FROM candidateList v WHERE CONCAT(v.id, v.name, v.place, v.post, v.gender,v.gmail) LIKE %?1%")
    public Page<candidateList> search(String keyword, Pageable Page);

    public Page<candidateList> findByAdminVerified(boolean b, Pageable pageRequest);
}
