package my.springcloud.account.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.entity.Authority;
import my.springcloud.account.domain.entity.MenuAuthority;
import my.springcloud.account.domain.repository.AuthorityRepository;
import my.springcloud.common.sec.model.CustomUserDetails;

@SuppressWarnings("rawtypes")
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAccessDecisionVoter implements AccessDecisionVoter {

	private static final List<String> ALLOWED_HTTP_METHODS_BYREADYN = Arrays.asList(
		HttpMethod.GET.name(), HttpMethod.OPTIONS.name(), HttpMethod.TRACE.name(), HttpMethod.HEAD.name()
	);
	private static final List<String> FREE_PASSES = Collections.singletonList("/v1/menus");
	private final AuthorityRepository authorityRepository;
	private final HttpServletRequest request;

	/**
	 * Indicates whether or not access is granted.
	 * <p>
	 * The decision must be affirmative ({@code ACCESS_GRANTED}), negative (
	 * {@code ACCESS_DENIED}) or the {@code AccessDecisionVoter} can abstain (
	 * {@code ACCESS_ABSTAIN}) from voting. Under no circumstances should implementing
	 * classes return any other value. If a weighting of results is desired, this should
	 * be handled in a custom
	 * {@link AccessDecisionManager} instead.
	 * <p>
	 * Unless an {@code AccessDecisionVoter} is specifically intended to vote on an access
	 * control decision due to a passed method invocation or configuration attribute
	 * parameter, it must return {@code ACCESS_ABSTAIN}. This prevents the coordinating
	 * {@code AccessDecisionManager} from counting votes from those
	 * {@code AccessDecisionVoter}s without a legitimate interest in the access control
	 * decision.
	 * <p>
	 * Whilst the secured object (such as a {@code MethodInvocation}) is passed as a
	 * parameter to maximise flexibility in making access control decisions, implementing
	 * classes should not modify it or cause the represented invocation to take place (for
	 * example, by calling {@code MethodInvocation.proceed()}).
	 *
	 * @param authentication the caller making the invocation
	 * @param object         the secured object being invoked
	 * @param collection     the configuration attributes associated with the secured object
	 * @return either {@link #ACCESS_GRANTED}, {@link #ACCESS_ABSTAIN} or
	 * {@link #ACCESS_DENIED}
	 */
	@Override
	@Transactional
	public int vote(Authentication authentication, Object object, Collection collection) {
		log.debug("> HTTP method: {}, URI: {}, free pass uris: {}", this.request.getMethod(),
			this.request.getRequestURI(), FREE_PASSES);

		if (FREE_PASSES.stream().anyMatch(fp -> this.request.getRequestURI().contains(fp))) {
			return ACCESS_GRANTED;
		}

		Object loginUser = authentication.getPrincipal();
		if (loginUser instanceof CustomUserDetails) {
			CustomUserDetails admin = (CustomUserDetails)loginUser;

			List<GrantedAuthority> authorities = new ArrayList<>(admin.getAuthorities());
			String role = authorities.get(0).getAuthority();

			long authorityId = Long.parseLong(StringUtils.delete(role, "ROLE_"));
			log.debug("> 권한 아이디(authorityId): {}, role: {}", authorityId, role);

			Optional<Authority> maybeAuthority = this.authorityRepository.findById(authorityId);
			if (maybeAuthority.isEmpty()) {
				return ACCESS_DENIED;
			}

			Authority authority = maybeAuthority.get();
			if (!Boolean.TRUE.equals(authority.getUseYn())) {
				return ACCESS_DENIED;
			}

			List<MenuAuthority> menuAuthorities = authority.getMenuAuthorities();

			int accessNum = menuAuthorities.stream()
				.filter(ma -> ma.getMenu().getMenuUrl() != null && this.request.getRequestURI()
					.contains(ma.getMenu().getMenuUrl()))
				.filter(ma -> Boolean.TRUE.equals(ma.isControlYn()) || (Boolean.TRUE.equals(ma.isReadYn())
					&& ALLOWED_HTTP_METHODS_BYREADYN.contains(this.request.getMethod())))
				.findAny()
				.map(ma -> ACCESS_GRANTED)
				.orElse(ACCESS_DENIED);

			log.debug("> accessNum: {}", accessNum);
			return accessNum;
		}

		return ACCESS_ABSTAIN;
	}

	/**
	 * Indicates whether this {@code AccessDecisionVoter} is able to vote on the passed
	 * {@code ConfigAttribute}.
	 * <p>
	 * This allows the {@code AbstractSecurityInterceptor} to check every configuration
	 * attribute can be consumed by the configured {@code AccessDecisionManager} and/or
	 * {@code RunAsManager} and/or {@code AfterInvocationManager}.
	 *
	 * @param attribute a configuration attribute that has been configured against the
	 *                  {@code AbstractSecurityInterceptor}
	 * @return true if this {@code AccessDecisionVoter} can support the passed
	 * configuration attribute
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * Indicates whether the {@code AccessDecisionVoter} implementation is able to provide
	 * access control votes for the indicated secured object type.
	 *
	 * @param clazz the class that is being queried
	 * @return true if the implementation can process the indicated class
	 */
	@Override
	public boolean supports(Class clazz) {
		return true;
	}

}
