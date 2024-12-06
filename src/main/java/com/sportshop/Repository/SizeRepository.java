package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<SizeEntity, String> {
    @Query("SELECT s FROM SizeEntity s WHERE s.name_size = :nameSize")
    Optional<SizeEntity> findByNameSize(@Param("nameSize") String nameSize);

    @Query("SELECT s FROM SizeEntity s WHERE s.name_size = :nameSize")
    SizeEntity findByName(@Param("nameSize") String nameSize);
}



