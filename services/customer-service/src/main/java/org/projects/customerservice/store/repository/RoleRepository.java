package org.projects.customerservice.store.repository;

import java.util.Optional;

import org.projects.customerservice.store.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(String roleUser);
}
