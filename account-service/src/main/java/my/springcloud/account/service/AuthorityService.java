package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.entity.Authority;
import my.springcloud.account.domain.entity.Menu;
import my.springcloud.account.domain.entity.MenuAuthority;
import my.springcloud.account.domain.repository.AuthorityRepository;
import my.springcloud.account.domain.repository.MenuAuthorityRepository;
import my.springcloud.account.domain.repository.MenuRepository;
import my.springcloud.account.domain.spec.AuthoritySpec;
import my.springcloud.account.mapper.AuthorityMapper;
import my.springcloud.account.mapper.MenuMapper;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.model.account.AuthorityDetail;
import my.springcloud.common.model.account.AuthorityHandle;
import my.springcloud.common.model.account.MenuDetail;
import my.springcloud.common.sec.model.CustomUserDetails;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthorityService {

	private final MenuRepository menuRepository;
	private final MenuMapper menuMapper;

	private final AuthorityRepository authorityRepository;
	private final AuthorityMapper authorityMapper;

	private final MenuAuthorityRepository menuAuthorityRepository;

	private Authority findById(long id) {
		return this.authorityRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * 권한 단건 조회
	 *
	 * @param id 권한 아이디
	 * @return 권한 상세
	 */
	@Transactional(readOnly = true)
	public AuthorityDetail find(long id) {
		AuthorityDetail authorityDetail = this.authorityMapper.toDto(this.findById(id));
		authorityDetail.convertXss();
		return authorityDetail;
	}

	/**
	 * 권한 목록 조회
	 *
	 * @param spec 검색 조건
	 * @param pageable 페이징
	 * @return 권한 목록 페이징
	 */
	@Transactional(readOnly = true)
	public Page<AuthorityDetail> find(AuthoritySpec spec, Pageable pageable) {
		return this.authorityRepository.findAll(spec, pageable).map(a -> {
			AuthorityDetail authorityDetail = this.authorityMapper.toDto(a);
			authorityDetail.convertXss();

			return authorityDetail;
		});
	}

	/**
	 * 권한 등록
	 *
	 * @param userDetails 로그인 사용자 데이타
	 * @param authorityHandle 권한 등록 데이타
	 * @return 권한 상세
	 */
	public AuthorityDetail create(@AuthenticationPrincipal UserDetails userDetails, AuthorityHandle authorityHandle) {
		CustomUserDetails loginUser = (CustomUserDetails) userDetails;
		authorityHandle.setRegId(loginUser.getUsername());
		authorityHandle.setUpdId(loginUser.getUsername());

		Authority authority = this.authorityMapper.toEntity(authorityHandle);
		this.authorityRepository.save(authority);

		List<Menu> menuList = this.menuRepository.findAllByParentMenuIdIsNullOrderBySortOrderAsc();
		List<MenuAuthority> menuAuthorities = new ArrayList<>();

		int i = 0;

		for (Menu m1 : menuList) {
			for (Menu m2 : m1.getMenus()) {
				MenuAuthority menuAuthority = new MenuAuthority();
				menuAuthority.setAuthorityId(authority.getAuthorityId());
				menuAuthority.setMenu(m2);
				menuAuthority.setReadYn(authorityHandle.getReadYn().get(i));
				menuAuthority.setControlYn(authorityHandle.getControlYn().get(i++));
				menuAuthorities.add(menuAuthority);
			}
		}

		authority.setMenuAuthorities(menuAuthorities);

		this.authorityRepository.save(authority);
		AuthorityDetail authorityDetail = this.authorityMapper.toDto(authority);
		authorityDetail.convertXss();

		return authorityDetail;
	}

	/**
	 * 권한 수정
	 *
	 * @param userDetails 로그인 사용자 데이타
	 * @param id 계정 아이디
	 * @param authorityHandle 수정할 권한 데이타
	 * @return 권한 상세
	 */
	public AuthorityDetail modify(@AuthenticationPrincipal UserDetails userDetails, long id, AuthorityHandle authorityHandle) {
		Authority authority = this.findById(id);
		authority.setUpdId(userDetails.getUsername());
		authority.setUpdDt(LocalDateTime.now());

		if (!this.authorityMapper.toDto(authority).equals(authorityHandle)) {
			authority.setAuthorityName(authorityHandle.getAuthorityName());
			authority.setDescription(authorityHandle.getDescription());
			authority.setUseYn(authorityHandle.getUseYn());
		}

		List<Menu> menus = this.menuRepository.findAllByParentMenuIdIsNullOrderBySortOrderAsc();

		int i = 0;

		for (Menu m1 : menus) {
			for (Menu m2 : m1.getMenus()) {
				MenuAuthority menuAuthority = this.menuAuthorityRepository.findByAuthorityIdAndMenuMenuId(id, m2.getMenuId());
				menuAuthority.setReadYn(authorityHandle.getReadYn().get(i));
				menuAuthority.setControlYn(authorityHandle.getControlYn().get(i++));
			}
		}

		AuthorityDetail authorityDetail = this.authorityMapper.toDto(authority);
		authorityDetail.convertXss();

		return authorityDetail;
	}

	/**
	 * 메뉴 목록 조회
	 *
	 * @return 메뉴 목록
	 */
	@Transactional(readOnly = true)
	public List<MenuDetail> findMenu() {
		return this.menuRepository.findAll().stream().map(this.menuMapper::toDto).collect(Collectors.toList());
	}

	/**
	 * 권한 전체 조회
	 *
	 * @return 권한 목록
	 */
	@Transactional(readOnly = true)
	public List<AuthorityDetail> findAll() {
		return this.authorityRepository.findAll().stream()
			.map(a -> {
				AuthorityDetail authorityDetail = this.authorityMapper.toDto(a);
				authorityDetail.convertXss();

				return authorityDetail;
			})
			.collect(Collectors.toList());
	}

}
