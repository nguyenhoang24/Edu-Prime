package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUserName(String username);

}
