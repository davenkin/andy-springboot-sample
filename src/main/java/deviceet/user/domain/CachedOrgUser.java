package deviceet.user.domain;

import lombok.Builder;

@Builder
public record CachedOrgUser(String id, String name) {
}
