package my.springcloud.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.repository.MenuRepository;
import my.springcloud.account.mapper.MenuMapper;
import my.springcloud.common.model.account.MenuDetail;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuMapper menuMapper;

	/**
	 * 메뉴 목록 조회
	 *
	 * @return 메뉴 목록
	 */
	@Transactional(readOnly = true)
	public List<MenuDetail> findMenu() {
		return this.menuRepository.findAll().stream().map(this.menuMapper::toDto).collect(Collectors.toList());
	}

}
