package my.springcloud.account.repository;

import my.springcloud.account.model.entity.MenuAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuAuthorityRepository extends JpaRepository<MenuAuthority, Long>, JpaSpecificationExecutor<MenuAuthority> {

    MenuAuthority findByAuthorityIdAndMenuMenuId(Long authorityId, Long menuId);

}