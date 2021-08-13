package my.springcloud.account.domain.repository;

import my.springcloud.account.domain.aggregate.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUsername(String username);

    List<Account> findByStatusAndAccountLockedDtBefore(String status, LocalDateTime beforeDt);

}