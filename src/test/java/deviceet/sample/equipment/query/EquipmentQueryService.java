package deviceet.sample.equipment.query;

import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.EquipmentSummary;
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
public class EquipmentQueryService {
    private final MongoTemplate mongoTemplate;
    private final EquipmentRepository equipmentRepository;

    public Page<QListedEquipment> listEquipments(ListEquipmentQuery listEquipmentQuery, Pageable pageable, Principal principal) {
        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());

        if (isNotBlank(listEquipmentQuery.search())) {
            criteria.and(Equipment.Fields.name).regex(listEquipmentQuery.search());
        }

        if (listEquipmentQuery.status() != null) {
            criteria.and(Equipment.Fields.status).is(listEquipmentQuery.status());
        }

        Query query = Query.query(criteria);
        query.fields().include(AggregateRoot.Fields.orgId,
                Equipment.Fields.name,
                Equipment.Fields.status,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy);

        long count = mongoTemplate.count(query, Equipment.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedEquipment> devices = mongoTemplate.find(query.with(pageable), QListedEquipment.class, EQUIPMENT_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }

    public QDetailedEquipment getEquipmentDetail(String equipmentId, Principal principal) {
        Equipment equipment = equipmentRepository.byId(equipmentId, principal.getOrgId());
        return QDetailedEquipment.builder()
                .id(equipment.getId())
                .orgId(equipment.getOrgId())
                .name(equipment.getName())
                .status(equipment.getStatus())
                .createdAt(equipment.getCreatedAt())
                .createdBy(equipment.getCreatedBy())
                .build();
    }

    public List<EquipmentSummary> getAllEquipmentSummaries(Principal principal) {
        return equipmentRepository.cachedEquipmentSummaries(principal.getOrgId());
    }
}
