package davenkin.springboot.web;

import davenkin.springboot.web.common.configuration.profile.BuildProfile;
import davenkin.springboot.web.common.domainevent.DomainEvent;
import davenkin.springboot.web.common.domainevent.consume.ConsumingDomainEvent;
import davenkin.springboot.web.common.domainevent.consume.DomainEventConsumer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

// Synchronously consume domain events without publishing them to the messaging middleware.
// It has the following advantages:
// 1. No need for messaging middle to set up, not even an embedded one
// 2. Avoid waiting for some criteria to met in order to do testing verification

@BuildProfile
@Aspect
@Component
@RequiredArgsConstructor
public class EnableSyncConsumeDomainEventAspect {
    private final DomainEventConsumer<DomainEvent> domainEventConsumer;

    @After("execution(* davenkin.springboot.web.common.domainevent.publish.PublishingDomainEventDao.stage(..))")
    public void consumeDomainEventSync(JoinPoint joinPoint) {
        if (joinPoint.getArgs()[0] instanceof List<?> events) {
            events.forEach((Object event) -> {
                DomainEvent theEvent = (DomainEvent) event;
                domainEventConsumer.consume(new ConsumingDomainEvent<>(theEvent.getId(), theEvent.getType().name(), theEvent));
            });
        }
    }
}
