package com.playstore.playstore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity , String> {
    Optional<StoreEntity> findByPlaystoreId(String appId);
}
