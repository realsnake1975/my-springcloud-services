package my.springcloud.account.service;

import my.springcloud.account.repository.MenuAuthorityRepository;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.dto.account.AuthorityDto;
import my.springcloud.common.model.dto.account.AuthorityHandleDto;
import my.springcloud.common.model.dto.account.MenuDto;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.account.model.aggregate.Authority;
import my.springcloud.account.model.entity.Menu;
import my.springcloud.account.model.entity.MenuAuthority;
import my.springcloud.account.model.mapper.AuthorityMapper;
import my.springcloud.account.model.mapper.MenuMapper;
import my.springcloud.account.model.spec.AuthoritySpec;
import my.springcloud.account.repository.AuthorityRepository;
import my.springcloud.account.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public AuthorityDto find(UserDetails userDetails, long id) {
        log.info("[REQ 권한 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        log.debug("> 권한 단건 조회 id: {}", id);
        Authority appMenu = this.findById(id);
        AuthorityDto authorityDto = this.authorityMapper.toDto(appMenu);
        authorityDto.convertXss();
        log.info("[RES 권한 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        return authorityDto;
    }

    /**
     * 권한 목록 조회
     *
     * @param spec
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<AuthorityDto> find(UserDetails userDetails, AuthoritySpec spec, Pageable pageable) {
        log.info("[REQ 권한 목록 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        Page<Authority> page = this.authorityRepository.findAll(spec, pageable);
        Page<AuthorityDto> pageDto = page.map(this.authorityMapper::toDto);
        log.info("[RES 권한 목록 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        return pageDto.map(authorityDto -> {
            authorityDto.convertXss();
            return authorityDto;
        });
    }

    /**
     * 권한 등록
     *
     * @param userDetails
     * @param authorityHandleDto
     * @return
     */
    @Transactional
    public AuthorityDto create(@AuthenticationPrincipal UserDetails userDetails, AuthorityHandleDto authorityHandleDto) {
        try{
            log.info("[REQ 권한 등록] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 권한 등록 authorityHandleDto: {}", authorityHandleDto.toString());
            CustomUserDetails loginUser = (CustomUserDetails) userDetails;
//        log.debug("> 인증(권한) 확인, username: {}, name: {}, roles: {}", loginUser.getUsername(), loginUser.getName(), loginUser.getAuthorities());

            authorityHandleDto.setRegId(loginUser.getUsername());
            authorityHandleDto.setUpdId(loginUser.getUsername());

            Authority authority = authorityMapper.toEntity(authorityHandleDto);
            authorityRepository.save(authority);

            List<Menu> menuList = menuRepository.findAllByParentMenuIdIsNullOrderBySortOrderAsc();
            List<MenuAuthority> menuAuthorityList = new ArrayList<>();

            int i = 0;

            for (Menu m1 : menuList) {
                for ( Menu m2 : m1.getMenuList() ) {
                    MenuAuthority menuAuthority = new MenuAuthority();
                    menuAuthority.setAuthorityId(authority.getAuthorityId());
                    menuAuthority.setMenu(m2);
                    menuAuthority.setReadYn(authorityHandleDto.getReadYn().get(i));
                    menuAuthority.setControlYn(authorityHandleDto.getControlYn().get(i++));
                    menuAuthorityList.add(menuAuthority);
                }
            }

            authority.setMenuAuthorityList(menuAuthorityList);

            authorityRepository.save(authority);
            AuthorityDto returnAuthorityDto = authorityMapper.toDto(authority);
            returnAuthorityDto.convertXss();

            return returnAuthorityDto;
        } catch (ServiceException se) {
            log.error(se.getResponseCodeType().desc());
            throw new ServiceException(se.getResponseCodeType(), se);
        } finally {
            log.info("[RES 권한 등록] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        }
    }

    /**
     * 권한 수정
     *
     * @param userDetails
     * @param id
     * @param authorityHandleDto
     * @return
     */
    @Transactional
    public AuthorityDto modify(@AuthenticationPrincipal UserDetails userDetails, long id, AuthorityHandleDto authorityHandleDto) {
        try {
            log.info("[REQ 권한 수정] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 권한 수정 id: {}, AuthorityHandleDto: {}", id, authorityHandleDto.toString());
            CustomUserDetails loginUser = (CustomUserDetails) userDetails;
//        log.debug("> 인증(권한) 확인, username: {}, name: {}, roles: {}", loginUser.getUsername(), loginUser.getName(), loginUser.getAuthorities());

            Authority authority = authorityRepository.getOne(id);

            authority.setUpdId(userDetails.getUsername());
            authority.setUpdDt(LocalDateTime.now());

            if (!authorityMapper.toDto(authority).equals(authorityHandleDto)) {
                authority.setAuthorityName(authorityHandleDto.getAuthorityName());
                authority.setDescription(authorityHandleDto.getDescription());
                authority.setUseYn(authorityHandleDto.getUseYn());
            }

            List<Menu> menuList = menuRepository.findAllByParentMenuIdIsNullOrderBySortOrderAsc();
            List<MenuAuthority> menuAuthorityList = new ArrayList<>();

            int i = 0;

            for (Menu m1 : menuList) {
                for ( Menu m2 : m1.getMenuList() ) {
                    MenuAuthority menuAuthority = menuAuthorityRepository.findByAuthorityIdAndMenuMenuId(id, m2.getMenuId());
                    menuAuthority.setReadYn(authorityHandleDto.getReadYn().get(i));
                    menuAuthority.setControlYn(authorityHandleDto.getControlYn().get(i++));
                }
            }
            AuthorityDto returnAuthorityDto = authorityMapper.toDto(authority);
            returnAuthorityDto.convertXss();

            return returnAuthorityDto;
        } catch (ServiceException se) {
            log.error(se.getResponseCodeType().desc());
            throw new ServiceException(se.getResponseCodeType(), se);
        } finally {
            log.info("[RES 권한 수정] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        }
    }

    /**
     * 메뉴 목록 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<MenuDto> findMenu() {
        log.info("[REQ 메뉴 목록 조회] 사용자ID: {}, url: {}", "", this.request.getRequestURL().toString());
        List<Menu> menuList = this.menuRepository.findAll();
        log.info("[RES 메뉴 목록 조회] 사용자ID: {}, url: {}", "", this.request.getRequestURL().toString());
        return menuList.stream().map(this.menuMapper::toDto).collect(Collectors.toList());
    }

    /**
     * 권한 전체 조회
     *
     * @return 권한 목록
     */
    @Transactional(readOnly = true)
    public List<AuthorityDto> findAll() {
        List<AuthorityDto> authorityDtoList = this.authorityRepository.findAll().stream().map(this.authorityMapper::toDto).collect(Collectors.toList());
        authorityDtoList.forEach(authorityDto -> authorityDto.convertXss());
        return authorityDtoList;
    }

}
