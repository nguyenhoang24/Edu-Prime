package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    Optional<Permission> findByPermissionName(String permissionName);
}
