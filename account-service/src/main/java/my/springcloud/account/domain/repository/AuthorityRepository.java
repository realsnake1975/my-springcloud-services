package my.springcloud.account.domain.repository;

import my.springcloud.account.domain.aggregate.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {

    Optional<Authority> findTop1ByAuthorityName(String name);

}