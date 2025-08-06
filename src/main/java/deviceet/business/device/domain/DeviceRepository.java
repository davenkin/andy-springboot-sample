package deviceet.business.device.domain;

import java.util.List;

public interface DeviceRepository {

    void save(Device device);

    Device byId(String id, String orgId);

    List<DeviceReference> cachedDeviceReferences(String orgId);
}
