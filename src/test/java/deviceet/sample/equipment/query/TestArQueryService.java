package deviceet.sample.equipment.query;

import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static deviceet.sample.equipment.domain.Equipment.EQUIPMENT_COLLECTION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class TestArQueryService {
    private final MongoTemplate mongoTemplate;

    public Page<QListedTestAr> listTestArs(ListTestArQuery listTestArQuery, Pageable pageable, Principal principal) {

        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());
        if (isNotBlank(listTestArQuery.search())) {
            criteria.and(Equipment.Fields.name).regex(listTestArQuery.search());
        }
        Query query = Query.query(criteria);
        query.fields().include(AggregateRoot.Fields.orgId,
                Equipment.Fields.name,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy);

        long count = mongoTemplate.count(query, Equipment.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedTestAr> devices = mongoTemplate.find(query.with(pageable), QListedTestAr.class, EQUIPMENT_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }
}
