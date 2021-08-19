package my.springcloud.account.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.entity.Menu;
import my.springcloud.account.domain.repository.MenuRepository;
import my.springcloud.account.mapper.MenuMapper;
import my.springcloud.common.model.account.MenuDetail;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuMapper menuMapper;

	private final HttpServletRequest request;

	/**
	 * 메뉴 목록 조회
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuDetail> findMenu() {
		log.info("[REQ ] 사용자ID: {}, url: {}", "", this.request.getRequestURL().toString());
		List<Menu> menuList = this.menuRepository.findAll();
		log.info("[RES ] 사용자ID: {}, url: {}", "", this.request.getRequestURL().toString());
		return menuList.stream().map(this.menuMapper::toDto).collect(Collectors.toList());
	}
}
