package com.ts.plateformcommunication.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE CONCAT(u.firstname, ' ', u.lastname) = :username")
  Optional<User> findByUsername(@Param("username") String username);
  Optional<User> findByEmail(String email);
  Boolean existsByEmail(String email);

}
