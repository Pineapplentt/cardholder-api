package com.jazztech.repository;

import com.jazztech.repository.entity.BankAccount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
}
