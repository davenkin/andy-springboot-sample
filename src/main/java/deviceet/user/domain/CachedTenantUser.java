package deviceet.user.domain;

import lombok.Builder;

@Builder
public record CachedTenantUser(String id, String name) {
}
