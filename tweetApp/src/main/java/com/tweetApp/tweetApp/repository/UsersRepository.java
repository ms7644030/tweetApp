package com.tweetApp.tweetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetApp.tweetApp.entities.UserModel;
import com.tweetApp.tweetApp.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,String> {
//	@Query(value = "Select u FROM Users u WHERE u.loginId=?1")
    Users findByLoginId(String loginId);
    Boolean existsByLoginId(String loginId);

  // @Query("{loginId : ?0, password : ?1}")
   @Query(value = "SELECT u FROM Users u WHERE u.loginId=?1 AND u.password =?2 ")
   Users findUserByUsernameAndPassword(String loginId, String password);
}
