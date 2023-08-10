package com.bit.springboard.repository;

import com.bit.springboard.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//update나 delete가 발생했을 때 곧장 커밋 롤백 처리
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    //select * from t_user
    //where user_id = :userId
    Optional<User> findByUserId(String userId);


    @Modifying
    @Query(value="update t_user" +
            "   set user_name = :userName",
            nativeQuery=true)
    public void updateUser(@Param("userName") String username);

    boolean existsByUserId(String userId);
}
