package deviceet.user.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record CachedOrgUsers(List<CachedOrgUser> users) {
}
