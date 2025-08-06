package deviceet.business.device.query;

import deviceet.business.device.domain.Device;
import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static deviceet.common.model.Role.ORG_IT_ADMIN;
import static deviceet.common.utils.Constants.DEVICE_COLLECTION;

@Component
@RequiredArgsConstructor
public class DeviceQueryService {
    private final MongoTemplate mongoTemplate;

    public Page<QListedDevice> listDevices(ListDeviceQuery listDeviceQuery, Pageable pageable, Principal principal) {
        principal.checkRole(ORG_IT_ADMIN);

        Criteria criteria = Criteria.where(AggregateRoot.Fields.orgId).is(principal.getOrgId());
        if (listDeviceQuery.cpuArchitecture() != null) {
            criteria.and(Device.Fields.cpuArchitecture).is(listDeviceQuery.cpuArchitecture());
        }
        if (listDeviceQuery.osType() != null) {
            criteria.and(Device.Fields.osType).is(listDeviceQuery.osType());
        }
        Query query = Query.query(criteria);
        query.fields().include(AggregateRoot.Fields.orgId,
                AggregateRoot.Fields.createdAt,
                AggregateRoot.Fields.createdBy,
                Device.Fields.reportedName,
                Device.Fields.configuredName,
                Device.Fields.cpuArchitecture,
                Device.Fields.osType);

        long count = mongoTemplate.count(query, Device.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedDevice> devices = mongoTemplate.find(query.with(pageable), QListedDevice.class, DEVICE_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }
}
