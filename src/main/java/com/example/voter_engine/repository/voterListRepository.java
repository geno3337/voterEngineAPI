package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.candidateList;
import com.example.voter_engine.Entity.voterList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface voterListRepository extends JpaRepository<voterList,Integer> {



    @Query(value = "select isVoted from voterList where voter_id=:id")
    int findIsVoteById(@Param("id")int id);

    @Transactional
    @Modifying
    @Query(value = "update voterList v set v.isVoted=1 where v.voter_id=:id")
    void updateIsVoted(@Param("id") int id);

    boolean existsByGmail(String gmail);

    @Query("SELECT v FROM voterList  v WHERE CONCAT(v.voter_id, v.voter_name, v.voter_age, v.gmail) LIKE %?1%")
    public Page<voterList> search(String keyword, Pageable Page);

}
