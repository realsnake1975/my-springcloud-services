package my.springcloud.account.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import my.springcloud.account.domain.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {

	Optional<Authority> findTop1ByAuthorityName(String name);

}