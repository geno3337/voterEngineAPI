package com.example.voter_engine.repository;

import com.example.voter_engine.Entity.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<user,Integer> {
    Optional<user> findByuserName(String userName);

    boolean existsByuserName(String name);


    user findByGmail(String gmail);

    user findByPasswordResetToken(String token);

    boolean existsByGmail(String gmail);

    boolean existsByPasswordResetToken(String token);

    @Query("SELECT v FROM user v WHERE CONCAT(v.userId, v.userName, v.gmail,v.role) LIKE %?1%")
    public Page<user> search(String keyword, Pageable pageRequest);
    

    user findByPassword(String currentPassword);

    boolean existsByPassword(String currentPassword);
}
