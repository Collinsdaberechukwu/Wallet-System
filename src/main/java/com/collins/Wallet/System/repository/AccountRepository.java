package com.collins.Wallet.System.repository;

import com.collins.Wallet.System.enums.AccountStatus;
import com.collins.Wallet.System.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByAccountStatusAndAccountNumberIsNull(AccountStatus accountStatus);
}
