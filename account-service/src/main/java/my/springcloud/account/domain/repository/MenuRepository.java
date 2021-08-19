package my.springcloud.account.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import my.springcloud.account.domain.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

	List<Menu> findAllByParentMenuIdIsNullOrderBySortOrderAsc();

}
