package my.springcloud.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import my.springcloud.account.domain.entity.AccountAttachFile;

public interface AccountAttachFileRepository extends JpaRepository<AccountAttachFile, String> {

	void deleteByAccountId(Long accountId);

}
