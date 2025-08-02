package deviceet.device.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.device.domain.Device;
import deviceet.device.domain.DeviceRepository;
import deviceet.device.domain.cache.CachedOrgDevice;
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
        this.cachedMongoDeviceRepository.evictCachedOrgDevices(device.getOrgId());
    }

    @Override
    public void save(List<Device> devices) {
        if (isEmpty(devices)) {
            return;
        }

        super.save(devices);
        this.cachedMongoDeviceRepository.evictCachedOrgDevices(devices.get(0).getOrgId());
    }

    @Override
    public void delete(Device device) {
        super.delete(device);
        this.cachedMongoDeviceRepository.evictCachedOrgDevices(device.getOrgId());
    }

    @Override
    public void delete(List<Device> devices) {
        if (isEmpty(devices)) {
            return;
        }

        super.delete(devices);
        this.cachedMongoDeviceRepository.evictCachedOrgDevices(devices.get(0).getOrgId());
    }

    @Override
    public List<CachedOrgDevice> cachedOrgDevices(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        return cachedMongoDeviceRepository.cachedOrgDevices(orgId).devices();
    }
}
