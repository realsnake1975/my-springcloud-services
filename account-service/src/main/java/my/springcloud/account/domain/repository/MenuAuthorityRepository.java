package my.springcloud.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import my.springcloud.account.domain.entity.MenuAuthority;

public interface MenuAuthorityRepository
	extends JpaRepository<MenuAuthority, Long>, JpaSpecificationExecutor<MenuAuthority> {

	MenuAuthority findByAuthorityIdAndMenuMenuId(Long authorityId, Long menuId);

}
