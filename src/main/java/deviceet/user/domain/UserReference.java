package deviceet.user.domain;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record UserReference(String id, String name) {
}
