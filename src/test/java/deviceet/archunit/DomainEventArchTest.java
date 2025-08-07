package deviceet.archunit;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import org.springframework.data.annotation.TypeAlias;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static deviceet.archunit.ArchUnitUtils.*;

@AnalyzeClasses(packages = "deviceet.business", importOptions = DoNotIncludeTests.class)
class DomainEventArchTest {

    @ArchTest
    static final ArchRule domainEventShouldResideInDomainEventPackage = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should()
            .resideInAnyPackage("deviceet.business..domain.event..")
            .because("Domain events are domain models and should have a specific package under domain.event package.");

    @ArchTest
    static final ArchRule concreteDomainEventShouldBeAnnotatedWithTypeAlias = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .and(areConcreteClasses())
            .should()
            .beAnnotatedWith(TypeAlias.class)
            .because("Concrete domain events should be annotated with @TypeAlias, otherwise the class FQCN will be used as type information and stored in database, which does not survive repackaging.");

    @ArchTest
    static final ArchRule concreateDomainEventShouldHavePrivateNoArgConstructor = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .and(areConcreteClasses())
            .should(havePrivateNoArgConstructor())
            .because("Private no-arg constructors of domain events are only used for deserialization, it should not be used for manual domain event creation because otherwise we might end up with invalid domain events. You may use @NoArgsConstructor(access = PRIVATE) for private constructors.");

    @ArchTest
    static final ArchRule domainEventShouldHaveNonPublicNoArgConstructor = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should(haveNonPublicNoArgConstructor())
            .because("Non-public no-arg constructors of domain events are only used for deserialization, it should not be used for manual domain event creation because otherwise we might end up with invalid domain events.You may use @NoArgsConstructor(access = PRIVATE) or @NoArgsConstructor(access = PROTECTED) for constructors.");

    @ArchTest
    static final ArchRule domainEventHandlerShouldResideInEventHandlerPackage = classes()
            .that()
            .areAssignableTo(AbstractEventHandler.class)
            .should()
            .resideInAnyPackage("deviceet.business..eventhandler..")
            .because("We should gather event handlers together under eventhandler package.");

    @ArchTest
    static final ArchRule domainEventShouldNotHaveBuilder = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should(notHaveBuilderMethod())
            .because("Domain events should be created using explict constructors but not builders, otherwise we might end up with invalid domain events.");

    @ArchTest
    static final ArchRule domainEventShouldNotHaveSetters = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should(notHaveSetterMethods())
            .because("Domain events should be immutable, hence it should not have setter methods");
}
