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

	public AccountSpec(String searchCondition, String searchKeyword) {
		super(searchCondition, searchKeyword);
	}

	@Override
	public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (TextUtils.isNotEmpty(super.getSearchKeyword())) {
			if (TextUtils.isNotEmpty(super.getSearchCondition())) {
				predicates.add(cb.like(root.get(super.getSearchCondition()), "%" + super.getSearchKeyword() + "%"));
			}
			else {
				Predicate accountNamePredicate = cb.like(root.get("accountName"), "%" + super.getSearchKeyword() + "%");
				Predicate usernamePredicate = cb.like(root.get("username"), "%" + super.getSearchKeyword() + "%");
				Predicate companyNamePredicate = cb.like(root.get("companyName"), "%" + super.getSearchKeyword() + "%");
				predicates.add(cb.or(accountNamePredicate, companyNamePredicate, usernamePredicate));
			}
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

}
