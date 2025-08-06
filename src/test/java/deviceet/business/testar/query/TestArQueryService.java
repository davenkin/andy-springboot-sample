package deviceet.business.testar.query;

import deviceet.business.testar.domain.TestAr;
import deviceet.common.model.AggregateRoot;
import deviceet.common.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static deviceet.business.testar.domain.TestAr.TEST_AR_COLLECTION;
import static deviceet.common.security.Role.ORG_IT_ADMIN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class TestArQueryService {
    private final MongoTemplate mongoTemplate;

    public Page<QListedTestAr> listTestArs(ListTestArQuery listTestArQuery, Pageable pageable, Principal principal) {
        principal.checkRole(ORG_IT_ADMIN);

        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());
        if (isNotBlank(listTestArQuery.search())) {
            criteria.and(TestAr.Fields.name).regex(listTestArQuery.search());
        }
        Query query = Query.query(criteria);
        query.fields().include(AggregateRoot.Fields.orgId,
                TestAr.Fields.name,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy);

        long count = mongoTemplate.count(query, TestAr.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedTestAr> devices = mongoTemplate.find(query.with(pageable), QListedTestAr.class, TEST_AR_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }
}
