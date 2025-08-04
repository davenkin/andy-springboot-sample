package deviceet.business.device.domain;

import deviceet.business.device.domain.cache.CachedOrgDevice;

import java.util.List;

public interface DeviceRepository {

    void save(Device device);

    Device byId(String id, String orgId);

    List<CachedOrgDevice> cachedOrgDevices(String orgId);
}
