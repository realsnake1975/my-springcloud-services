package my.springcloud.account.domain.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.common.model.account.AccountSearch;
import my.springcloud.common.utils.TextUtils;

public class AccountSpec extends AccountSearch implements Specification<Account> {

	private static final long serialVersionUID = 7975607789129411879L;

	public AccountSpec(String searchCondition, String searchKeyword, List<String> searchType) {
		super(searchCondition, searchKeyword, searchType);
	}

	@Override
	public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		Predicate keywordPredicate;
		Predicate authorityPredicate;

		if (!super.getSearchType().isEmpty()) {
			if (TextUtils.isNotEmpty(super.getSearchCondition())) {
				keywordPredicate = cb.like(root.get(super.getSearchCondition()), "%" + super.getSearchKeyword() + "%");
			} else {
				Predicate namePredicate = cb.like(root.get("accountName"), "%" + super.getSearchKeyword() + "%");
				Predicate usernamePredicate = cb.like(root.get("username"), "%" + super.getSearchKeyword() + "%");
				Predicate companyNamePredicate = cb.like(root.get("companyName"), "%" + super.getSearchKeyword() + "%");
				keywordPredicate = cb.or(namePredicate, companyNamePredicate, usernamePredicate);
			}

			authorityPredicate = root.get("authority").get("authorityName").in(super.getSearchType());

			predicates.add(cb.and(keywordPredicate, authorityPredicate));
		} else {
			//검색결과 없음
			predicates.add(cb.equal(root.get("accountId"), "0"));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

}
