package com.collins.Wallet.System.repository;

import com.collins.Wallet.System.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotencyKey,Long> {

    Optional<IdempotencyKey> findByIdempotencyKey(String idempotencyKey);
}
