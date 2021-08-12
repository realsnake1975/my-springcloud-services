package my.springcloud.account.service;

import my.springcloud.account.model.aggregate.Account;
import my.springcloud.account.model.aggregate.Authority;
import my.springcloud.account.model.entity.Menu;
import my.springcloud.account.model.entity.MenuAuthority;
import my.springcloud.account.repository.AccountRepository;
import my.springcloud.account.repository.MenuRepository;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.RoleType;
import my.springcloud.account.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitializationService implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String REG_USER_ID = "SYSTEM";

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    @Transactional
    public void run(String... args) {
        List<Menu> menus = this.saveInitMenu();
        Authority saAuth = this.saveInitAuthorities(menus);
        this.saveInitAdminAccount(saAuth);
    }

    @Transactional
    public List<Menu> saveInitMenu() {
        List<Menu> menus = this.menuRepository.findAll();
        if (menus.isEmpty()) {
            menus.addAll(this.saveInitProductMgt());
            menus.addAll(this.saveInitServiceMgt());
            menus.addAll(this.saveInitOperationMgt());
        }

        return menus;
    }

    @Transactional
    public Authority saveInitAuthorities(List<Menu> menus) {
        Optional<Authority> maybeSuperadmin = this.authorityRepository.findTop1ByAuthorityName(RoleType.SUPERADMIN.getName());
        LocalDateTime now = LocalDateTime.now();

        Authority saAuth;
        if (maybeSuperadmin.isPresent()) {
            saAuth = maybeSuperadmin.get();
        }
        else {
            saAuth = new Authority();
            saAuth.setAuthorityName(RoleType.SUPERADMIN.getName());
            saAuth.setDescription("유플러스 사업팀");
            saAuth.setUseYn(true);
            saAuth.setRegId(REG_USER_ID);
            saAuth.setRegDt(now);
            saAuth.setUpdId(REG_USER_ID);
            saAuth.setUpdDt(now);
            this.authorityRepository.save(saAuth);
        }

        if (saAuth.getMenuAuthorityList().isEmpty()) {
            List<MenuAuthority> menuAuthorities = menus.stream().map(m -> {
                MenuAuthority menuAuthority = new MenuAuthority();
                menuAuthority.setAuthorityId(saAuth.getAuthorityId());
                menuAuthority.setMenu(m);
                menuAuthority.setControlYn(true);
                menuAuthority.setReadYn(true);
                return menuAuthority;
            }).collect(Collectors.toList());

            saAuth.getMenuAuthorityList().addAll(menuAuthorities);
        }

        Optional<Authority> maybeOperator = this.authorityRepository.findTop1ByAuthorityName(RoleType.OPERATOR.getName());
        if (!maybeOperator.isPresent()) {
            Authority operator = new Authority();
            operator.setAuthorityName(RoleType.OPERATOR.getName());
            operator.setDescription("유플러스 사업팀");
            operator.setUseYn(true);
            operator.setRegId(REG_USER_ID);
            operator.setRegDt(now);
            operator.setUpdId(REG_USER_ID);
            operator.setUpdDt(now);
            this.authorityRepository.save(operator);
        }

        return saAuth;
    }

    @Transactional
    public void saveInitAdminAccount(Authority saAuth) {
        List<Account> accounts = this.accountRepository.findAll();
        boolean nonExistsSueradmin = accounts.stream().noneMatch(a -> a.getAuthority().getAuthorityName().equals(RoleType.SUPERADMIN.getName()));
        boolean nonExistsUsernameAdmin = accounts.stream().noneMatch(a -> a.getUsername().equals("admin"));

        if (nonExistsSueradmin && nonExistsUsernameAdmin) {
            LocalDateTime now = LocalDateTime.now();

            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(this.passwordEncoder.encode("admin123"));
            adminAccount.setAccountName("관리자");
            adminAccount.setCompanyName("유플러스");
            adminAccount.setPhoneNumber("00000000000");
            adminAccount.setEmail("email@uplus.com");
            adminAccount.setStatus(AccountStatusType.APPROVAL.code());
            adminAccount.setRegId(REG_USER_ID);
            adminAccount.setRegDt(now);
            adminAccount.setUpdId(REG_USER_ID);
            adminAccount.setUpdDt(now);
            adminAccount.setAuthority(saAuth);

            this.accountRepository.save(adminAccount);
        }
    }

    private List<Menu> saveInitProductMgt() {
        Menu menu = new Menu();
        menu.setDepth(1);
        menu.setMenuName("상품관리");
        menu.setSortOrder(1);

        Menu subMenu1 = new Menu();
        subMenu1.setMenuId(menu.getMenuId());
        subMenu1.setDepth(2);
        subMenu1.setMenuName("튜토리얼관리");
        subMenu1.setMenuUrl("/v1/tutorials");
        subMenu1.setSortOrder(1);

        Menu subMenu2 = new Menu();
        subMenu2.setMenuId(menu.getMenuId());
        subMenu2.setDepth(2);
        subMenu2.setMenuName("홈쇼핑사관리");
        subMenu2.setMenuUrl("/v1/shoppingchannel");
        subMenu2.setSortOrder(2);

        menu.getMenuList().addAll(Arrays.asList(subMenu1, subMenu2));

        this.menuRepository.save(menu);

        return Arrays.asList(menu, subMenu1, subMenu2);
    }

    private List<Menu> saveInitServiceMgt() {
        Menu menu = new Menu();
        menu.setDepth(1);
        menu.setMenuName("서비스관리");
        menu.setSortOrder(2);

        Menu subMenu1 = new Menu();
        subMenu1.setMenuId(menu.getMenuId());
        subMenu1.setDepth(2);
        subMenu1.setMenuName("메뉴관리");
        subMenu1.setMenuUrl("/v1/appmenus");
        subMenu1.setSortOrder(1);

        Menu subMenu2 = new Menu();
        subMenu2.setMenuId(menu.getMenuId());
        subMenu2.setDepth(2);
        subMenu2.setMenuName("컨테이너관리");
        subMenu2.setMenuUrl("/v1/containers");
        subMenu2.setSortOrder(2);

        Menu subMenu3 = new Menu();
        subMenu3.setMenuId(menu.getMenuId());
        subMenu3.setDepth(2);
        subMenu3.setMenuName("카테고리관리");
        subMenu3.setMenuUrl("/v1/category");
        subMenu3.setSortOrder(3);

        Menu subMenu4 = new Menu();
        subMenu4.setMenuId(menu.getMenuId());
        subMenu4.setDepth(2);
        subMenu4.setMenuName("편성조회");
        subMenu4.setMenuUrl("/v1/mainproduct");
        subMenu4.setSortOrder(4);

        menu.getMenuList().addAll(Arrays.asList(subMenu1, subMenu2, subMenu3, subMenu4));

        this.menuRepository.save(menu);

        return Arrays.asList(menu, subMenu1, subMenu2, subMenu3, subMenu4);
    }

    private List<Menu> saveInitOperationMgt() {
        Menu menu = new Menu();
        menu.setDepth(1);
        menu.setMenuName("운영관리");
        menu.setSortOrder(3);

        Menu subMenu1 = new Menu();
        subMenu1.setMenuId(menu.getMenuId());
        subMenu1.setDepth(2);
        subMenu1.setMenuName("계정관리");
        subMenu1.setMenuUrl("/v1/accounts");
        subMenu1.setSortOrder(1);

        Menu subMenu2 = new Menu();
        subMenu2.setMenuId(menu.getMenuId());
        subMenu2.setDepth(2);
        subMenu2.setMenuName("그룹관리");
        subMenu2.setMenuUrl("/v1/authority");
        subMenu2.setSortOrder(2);

        menu.getMenuList().addAll(Arrays.asList(subMenu1, subMenu2));

        this.menuRepository.save(menu);

        return Arrays.asList(menu, subMenu1, subMenu2);
    }

}
