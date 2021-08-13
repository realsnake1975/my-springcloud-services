package my.springcloud.account.domain.repository;

import my.springcloud.account.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    List<Menu> findAllByParentMenuIdIsNullOrderBySortOrderAsc();

}