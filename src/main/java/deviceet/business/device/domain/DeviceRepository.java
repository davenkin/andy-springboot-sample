package deviceet.business.device.domain;

import deviceet.business.device.domain.cache.CachedDeviceReference;

import java.util.List;

public interface DeviceRepository {

    void save(Device device);

    Device byId(String id, String orgId);

    List<CachedDeviceReference> cachedDeviceReferences(String orgId);
}
