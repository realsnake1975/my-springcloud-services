package my.springcloud.common.sec.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = -5040562823453196756L;

	private final String username;

	@JsonIgnore
	private String password;

	private final Set<GrantedAuthority> authorities;

	// 이름
	private final String name;

	// 계정 아이디
	private Long accountId;

	@Setter
	private boolean nonExpiredPasswordYn = true;

	public CustomUserDetails(String username, String name, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.name = name;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	public CustomUserDetails(String username, String password, String name, Collection<? extends GrantedAuthority> authorities) {
		this(username, name, authorities);
		this.password = password;
    }

	public CustomUserDetails(String username, String name, Collection<? extends GrantedAuthority> authorities, Long accountId) {
		this(username, name, authorities);
		this.accountId = accountId;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * 권한 정렬
	 *
	 * @param authorities 권한 목록
	 * @return 정렬된 권한 셋
	 */
	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			if (g2.getAuthority() == null) {
				return -1;
			}
			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}

}
