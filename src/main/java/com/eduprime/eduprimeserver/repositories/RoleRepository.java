package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(String roleName);
}
