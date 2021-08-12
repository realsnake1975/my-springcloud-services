package my.springcloud.account.model.spec;

import my.springcloud.common.utils.TextUtils;
import my.springcloud.account.model.aggregate.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AccountSpec implements Serializable, Specification<Account> {

	private static final long serialVersionUID = 7975607789129411879L;

	// 검색조건 정의
	private final String searchCondition;
	private final String searchKeyword;
	private final List<String> searchType;

	@Override
	public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		Predicate keywordPredicate;

//		List<String> searchType = Arrays.asList(this.searchType);

//		if(!(this.searchType == null)) {
//			searchType = Arrays.asList(this.searchType.split(","));
//		}

		Predicate authorityPredicate;

		if(searchType.size() > 0) {
			if(TextUtils.isNotEmpty(searchCondition)) {
				keywordPredicate = cb.like(root.get(searchCondition), "%" + searchKeyword + "%");
			} else {
				Predicate namePredicate = cb.like(root.get("accountName"), "%" + searchKeyword + "%");
				Predicate usernamePredicate = cb.like(root.get("username"), "%" + searchKeyword + "%");
				Predicate companyNamePredicate = cb.like(root.get("companyName"), "%" + searchKeyword + "%");
				keywordPredicate = cb.or(namePredicate, companyNamePredicate, usernamePredicate);
			}

			authorityPredicate = root.get("authority").get("authorityName").in(searchType);

			predicates.add(cb.and(keywordPredicate, authorityPredicate));
		} else {
			//검색결과 없음
			predicates.add(cb.equal(root.get("accountId"), "0"));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

}
