package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.account.domain.entity.Authority;
import my.springcloud.account.domain.entity.Menu;
import my.springcloud.account.domain.entity.MenuAuthority;
import my.springcloud.account.domain.repository.AccountRepository;
import my.springcloud.account.domain.repository.AuthorityRepository;
import my.springcloud.account.domain.repository.MenuRepository;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.RoleType;

@Slf4j
@RequiredArgsConstructor
@Service
public class InitializationService implements CommandLineRunner {

	private static final String REG_USER_ID = "SYSTEM";
	private final MenuRepository menuRepository;
	private final AuthorityRepository authorityRepository;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 */
	@Override
	@Transactional
	public void run(String... args) {
		List<Menu> menus = this.saveOrGetMenus();
		Authority saAuth = this.saveOrGetAuthorities(menus);
		this.saveAdminAccountIfEmpty(saAuth);
	}

	@Transactional
	public List<Menu> saveOrGetMenus() {
		List<Menu> menus = this.menuRepository.findAll();
		if (menus.isEmpty()) {
			menus.addAll(this.saveDefaultMenu());
		}

		return menus;
	}

	@Transactional
	public Authority saveOrGetAuthorities(List<Menu> menus) {
		Optional<Authority> maybeSuperadmin = this.authorityRepository.findTop1ByAuthorityName(RoleType.SUPERADMIN.getName());
		LocalDateTime now = LocalDateTime.now();

		Authority saAuth;
		if (maybeSuperadmin.isPresent()) {
			saAuth = maybeSuperadmin.get();
		} else {
			saAuth = new Authority();
			saAuth.setAuthorityName(RoleType.SUPERADMIN.getName());
			saAuth.setDescription("서비스기획팀");
			saAuth.setUseYn(true);
			saAuth.setRegId(REG_USER_ID);
			saAuth.setRegDt(now);
			saAuth.setUpdId(REG_USER_ID);
			saAuth.setUpdDt(now);
			this.authorityRepository.save(saAuth);
		}

		if (saAuth.getMenuAuthorities().isEmpty()) {
			List<MenuAuthority> menuAuthorities = menus.stream().map(m -> {
				MenuAuthority menuAuthority = new MenuAuthority();
				menuAuthority.setAuthorityId(saAuth.getAuthorityId());
				menuAuthority.setMenu(m);
				menuAuthority.setControlYn(true);
				menuAuthority.setReadYn(true);
				return menuAuthority;
			}).collect(Collectors.toList());

			saAuth.getMenuAuthorities().addAll(menuAuthorities);
		}

		Optional<Authority> maybeOperator = this.authorityRepository.findTop1ByAuthorityName(RoleType.OPERATOR.getName());
		if (maybeOperator.isEmpty()) {
			Authority operator = new Authority();
			operator.setAuthorityName(RoleType.OPERATOR.getName());
			operator.setDescription("서비스기획팀");
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
	public void saveAdminAccountIfEmpty(Authority saAuth) {
		List<Account> accounts = this.accountRepository.findAll();
		boolean nonExistsSueradmin = accounts.stream().noneMatch(a -> a.getAuthority().getAuthorityName().equals(RoleType.SUPERADMIN.getName()));
		boolean nonExistsUsernameAdmin = accounts.stream().noneMatch(a -> a.getUsername().equals("admin"));

		if (nonExistsSueradmin && nonExistsUsernameAdmin) {
			LocalDateTime now = LocalDateTime.now();

			Account adminAccount = new Account();
			adminAccount.setUsername("admin");
			adminAccount.setPassword(this.passwordEncoder.encode("!@#Admin123"));
			adminAccount.setAccountName("관리자");
			adminAccount.setCompanyName("(주)리얼스네이크");
			adminAccount.setPhoneNumber("00000000000");
			adminAccount.setEmail("realsnake1975@gmail.com");
			adminAccount.setStatus(AccountStatusType.APPROVAL.code());
			adminAccount.setRegId(REG_USER_ID);
			adminAccount.setRegDt(now);
			adminAccount.setUpdId(REG_USER_ID);
			adminAccount.setUpdDt(now);
			adminAccount.setAuthority(saAuth);

			this.accountRepository.save(adminAccount);
		}
	}

	private List<Menu> saveDefaultMenu() {
		Menu menu = new Menu();
		menu.setDepth(1);
		menu.setMenuName("운영관리");
		menu.setSortOrder(1);

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
		subMenu2.setMenuUrl("/v1/authorities");
		subMenu2.setSortOrder(2);

		menu.getMenus().addAll(Arrays.asList(subMenu1, subMenu2));

		this.menuRepository.save(menu);

		return Arrays.asList(menu, subMenu1, subMenu2);
	}

}
