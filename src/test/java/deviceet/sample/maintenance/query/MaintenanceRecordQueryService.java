package deviceet.sample.maintenance.query;

import deviceet.common.exception.ServiceException;
import deviceet.common.model.AggregateRoot;
import deviceet.common.model.principal.Principal;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static deviceet.common.exception.ErrorCode.MAINTENANCE_RECORD_NOT_FOUND;
import static deviceet.common.util.Constants.*;
import static deviceet.common.util.NullableMapUtils.mapOf;
import static deviceet.sample.maintenance.domain.MaintenanceRecord.MAINTENANCE_RECORD_COLLECTION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class MaintenanceRecordQueryService {
    private final MongoTemplate mongoTemplate;

    public Page<QListedMaintenanceRecord> listMaintenanceRecords(ListMaintenanceRecordsQuery listQuery,
                                                                 Pageable pageable,
                                                                 Principal principal) {
        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());

        if (isNotBlank(listQuery.search())) {
            criteria.orOperator(where(MaintenanceRecord.Fields.equipmentName).regex(listQuery.search()),
                    where(MaintenanceRecord.Fields.description).regex(listQuery.search()));
        }

        if (listQuery.status() != null) {
            criteria.and(MaintenanceRecord.Fields.status).is(listQuery.status());
        }

        Query query = Query.query(criteria);
        query.fields().include(
                MaintenanceRecord.Fields.equipmentId,
                MaintenanceRecord.Fields.equipmentName,
                MaintenanceRecord.Fields.status,
                AggregateRoot.Fields.orgId,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy);

        long count = mongoTemplate.count(query, MaintenanceRecord.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedMaintenanceRecord> devices = mongoTemplate.find(query.with(pageable), QListedMaintenanceRecord.class, MAINTENANCE_RECORD_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }

    public QDetailedMaintenanceRecord getMaintenanceRecordDetail(String maintenanceRecordId, Principal principal) {
        Query query = Query.query(where(MONGO_ID).is(maintenanceRecordId).and(ORG_ID).is(principal.getOrgId()));

        query.fields().include(
                MaintenanceRecord.Fields.equipmentId,
                MaintenanceRecord.Fields.equipmentName,
                MaintenanceRecord.Fields.status,
                MaintenanceRecord.Fields.description,
                AggregateRoot.Fields.orgId,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy);
        QDetailedMaintenanceRecord record = mongoTemplate.findOne(query, QDetailedMaintenanceRecord.class, MAINTENANCE_RECORD_COLLECTION);

        if (record == null) {
            throw new ServiceException(MAINTENANCE_RECORD_NOT_FOUND, "Not found.",
                    mapOf(ID, maintenanceRecordId));
        }

        return record;
    }
}
