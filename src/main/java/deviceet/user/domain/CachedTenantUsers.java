package deviceet.user.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record CachedTenantUsers(List<CachedTenantUser> users) {
}
