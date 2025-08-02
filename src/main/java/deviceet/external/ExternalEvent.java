package deviceet.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Data
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public abstract class ExternalEvent {
    private String id;
    private String type;
    private String orgId;
}
