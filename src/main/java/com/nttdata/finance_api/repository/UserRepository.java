package com.nttdata.finance_api.repository;

import com.nttdata.finance_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
