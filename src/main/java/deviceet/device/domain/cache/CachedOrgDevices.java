package deviceet.device.domain.cache;

import lombok.Builder;

import java.util.List;

@Builder
public record CachedOrgDevices(List<CachedOrgDevice> devices) {
}
