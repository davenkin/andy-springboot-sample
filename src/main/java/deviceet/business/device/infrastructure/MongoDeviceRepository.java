package deviceet.business.device.infrastructure;

import deviceet.business.device.domain.Device;
import deviceet.business.device.domain.DeviceRepository;
import deviceet.business.device.domain.cache.CachedDeviceReference;
import deviceet.common.infrastructure.AbstractMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class MongoDeviceRepository extends AbstractMongoRepository<Device> implements DeviceRepository {
    private final CachedMongoDeviceRepository cachedMongoDeviceRepository;

    @Override
    public void save(Device device) {
        super.save(device);
        this.cachedMongoDeviceRepository.evictCachedDeviceReferences(device.getOrgId());
    }

    @Override
    public void save(List<Device> devices) {
        if (isEmpty(devices)) {
            return;
        }

        super.save(devices);
        this.cachedMongoDeviceRepository.evictCachedDeviceReferences(devices.get(0).getOrgId());
    }

    @Override
    public void delete(Device device) {
        super.delete(device);
        this.cachedMongoDeviceRepository.evictCachedDeviceReferences(device.getOrgId());
    }

    @Override
    public void delete(List<Device> devices) {
        if (isEmpty(devices)) {
            return;
        }

        super.delete(devices);
        this.cachedMongoDeviceRepository.evictCachedDeviceReferences(devices.get(0).getOrgId());
    }

    @Override
    public List<CachedDeviceReference> cachedDeviceReferences(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        return cachedMongoDeviceRepository.cachedDeviceReferences(orgId);
    }
}
