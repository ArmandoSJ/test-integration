package com.technologyos.unittest.repositories;

import com.technologyos.unittest.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   UserEntity findByEmail(String email);

   UserEntity findByUserId(String userId);

   UserEntity findByEmailEndsWith(String email);

   @Query("select user from UserEntity user where user.email like %:emailDomain")
   List<UserEntity> findUsersWithEmailEndingWith(@Param("emailDomain") String emailDomain);
}
