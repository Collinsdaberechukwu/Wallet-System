package com.collins.Wallet.System.repository;


import com.collins.Wallet.System.model.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance,Long> {

}
