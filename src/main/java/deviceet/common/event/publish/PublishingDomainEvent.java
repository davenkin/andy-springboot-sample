package deviceet.common.event.publish;

import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

import static deviceet.common.event.publish.DomainEventPublishStatus.CREATED;
import static deviceet.common.utils.Constants.PUBLISHING_EVENT_COLLECTION;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

// DomainEvent wrapper for publishing, added status and publishCount to track the publishing process
@Getter
@FieldNameConstants
@NoArgsConstructor(access = PRIVATE)
@Document(PUBLISHING_EVENT_COLLECTION)
@TypeAlias("PUBLISHING_DOMAIN_EVENT")
public class PublishingDomainEvent {
    private String id;
    private DomainEvent event;
    private DomainEventPublishStatus status;
    private int publishedCount;
    private Instant raisedAt;

    public PublishingDomainEvent(DomainEvent event) {
        requireNonNull(event, "Domain event must not be null.");

        this.id = event.getId();
        this.event = event;
        this.status = CREATED;
        this.publishedCount = 0;
        this.raisedAt = event.getRaisedAt();
    }
}
