package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<candidate,Integer> {

    @Query(value="select vote from candidate where id = :id")
    int findVoteById(@Param("id") int id);

    @Query(value = "select max(vote) from candidate")
     int max();

    @Transactional
    @Modifying
    @Query(value = "update candidate v set v.vote= :vote where v.id = :v_id")
    void updateVote(@Param("vote") int vote, @Param("v_id") int v_id);


    @Query("SELECT v FROM candidate v WHERE CONCAT(v.id, v.name, v.place, v.post, v.gender,v.gmail) LIKE %?1%")
    public Page<candidate> search(String keyword,Pageable Page);

    @Query("SELECT v FROM candidate v WHERE CONCAT(v.id, v.name,v.vote) LIKE %?1%")
    public Page<candidate> searchWinner(String keyword,Pageable Page);

    candidate findByVote(int max);

    boolean existsByGmail(String gmail);


//    Page<candidate> findAll(Pageable pageable);


}
