package my.springcloud.account.domain.spec;

import my.springcloud.account.domain.aggregate.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuthoritySpec implements Specification<Authority> {

	private static final long serialVersionUID = 7975607789129411879L;

	@Override
	public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		return cb.and(predicates.toArray(new Predicate[0]));
	}

}