package deviceet.archunit;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import org.springframework.data.annotation.TypeAlias;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static deviceet.archunit.ArchUnitUtils.havePrivateNoArgConstructor;

@AnalyzeClasses(packages = "deviceet.business", importOptions = DoNotIncludeTests.class)
public class DomainEventArchTest {

    @ArchTest
    public static final ArchRule domainEventLocation = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should()
            .resideInAnyPackage("deviceet.business..domain.event..")
            .because("Domain events belongs domain model, and should have a specific package for it.");

    @ArchTest
    public static final ArchRule domainEventShouldAnnotatedWithTypeAlias = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should()
            .beAnnotatedWith(TypeAlias.class)
            .because("In Mongo, @TypeAlias set the type of DomainEvent to some fixed names, rather than using class FQCN which does not survive repackaging.");

    @ArchTest
    public static final ArchRule domainEventShouldHaveNoConstructor = classes()
            .that()
            .areAssignableTo(DomainEvent.class)
            .should(havePrivateNoArgConstructor())
            .because("Private no arg constructors(you can use @NoArgsConstructor(access = PRIVATE)) of domain events are used for Jackson deserialization, but it should not be used to create domain event object because it can easily result in invalid object.");

    @ArchTest
    public static final ArchRule domainEventHandlerLocation = classes()
            .that()
            .areAssignableTo(AbstractEventHandler.class)
            .should()
            .resideInAnyPackage("deviceet.business..eventhandler..")
            .because("We should gather event handlers together.");

}
