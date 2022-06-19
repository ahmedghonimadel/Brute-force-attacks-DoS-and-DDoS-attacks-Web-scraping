package com.stackdeans.loginregister.repository;

import com.stackdeans.loginregister.models.ERole;
import com.stackdeans.loginregister.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * @author Ghonim
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
