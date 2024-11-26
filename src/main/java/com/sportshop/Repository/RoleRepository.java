package com.sportshop.Repository;

import com.sportshop.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    RoleEntity findByName(String roleName);

    @Query("SELECT r FROM RoleEntity r WHERE r.name = :name")
    RoleEntity findByNameWithoutAccounts(@Param("name") String name);
}
