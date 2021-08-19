package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.aggregate.Authority;
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
@Service
@RequiredArgsConstructor
public class AuthorityService {

	private final MenuRepository menuRepository;
	private final MenuMapper menuMapper;

	private final AuthorityRepository authorityRepository;
	private final AuthorityMapper authorityMapper;

	private final MenuAuthorityRepository menuAuthorityRepository;

	private final HttpServletRequest request;

	private Authority findById(long id) {
		return this.authorityRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * 권한 단건 조회
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AuthorityDetail find(UserDetails userDetails, long id) {
		log.info("[REQ 권한 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());

		Authority appMenu = this.findById(id);
		AuthorityDetail authorityDetail = this.authorityMapper.toDto(appMenu);
		authorityDetail.convertXss();

		log.info("[RES 권한 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		return authorityDetail;
	}

	/**
	 * 권한 목록 조회
	 *
	 * @param spec
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<AuthorityDetail> find(UserDetails userDetails, AuthoritySpec spec, Pageable pageable) {
		log.info("[REQ 권한 목록 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());

		Page<AuthorityDetail> authorityPage = this.authorityRepository.findAll(spec, pageable).map(a -> {
			AuthorityDetail authorityDetail = this.authorityMapper.toDto(a);
			authorityDetail.convertXss();

			return authorityDetail;
		});

		log.info("[RES 권한 목록 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		return authorityPage;
	}

	/**
	 * 권한 등록
	 *
	 * @param userDetails
	 * @param authorityHandle
	 * @return
	 */
	@Transactional
	public AuthorityDetail create(@AuthenticationPrincipal UserDetails userDetails, AuthorityHandle authorityHandle) {
		log.info("[REQ 권한 등록] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

		CustomUserDetails loginUser = (CustomUserDetails)userDetails;
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

		log.info("[RES 권한 등록] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
		return authorityDetail;
	}

	/**
	 * 권한 수정
	 *
	 * @param userDetails
	 * @param id
	 * @param authorityHandle
	 * @return
	 */
	@Transactional
	public AuthorityDetail modify(@AuthenticationPrincipal UserDetails userDetails, long id,
		AuthorityHandle authorityHandle) {
		log.info("[REQ 권한 수정] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

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
				MenuAuthority menuAuthority = this.menuAuthorityRepository.findByAuthorityIdAndMenuMenuId(id,
					m2.getMenuId());
				menuAuthority.setReadYn(authorityHandle.getReadYn().get(i));
				menuAuthority.setControlYn(authorityHandle.getControlYn().get(i++));
			}
		}

		AuthorityDetail authorityDetail = this.authorityMapper.toDto(authority);
		authorityDetail.convertXss();

		log.info("[RES 권한 수정] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
		return authorityDetail;
	}

	/**
	 * 메뉴 목록 조회
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuDetail> findMenu() {
		log.info("[REQ 메뉴 목록 조회] 사용자 ID: {}, url: {}", "", this.request.getRequestURL().toString());

		List<Menu> menus = this.menuRepository.findAll();

		log.info("[RES 메뉴 목록 조회] 사용자 ID: {}, url: {}", "", this.request.getRequestURL().toString());
		return menus.stream().map(this.menuMapper::toDto).collect(Collectors.toList());
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
