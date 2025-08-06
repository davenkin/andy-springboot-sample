package deviceet.archunit;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import deviceet.common.model.AggregateRoot;
import org.springframework.data.annotation.TypeAlias;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static deviceet.archunit.ArchUnitUtils.haveNoBuilderMethod;
import static deviceet.archunit.ArchUnitUtils.havePrivateNoArgConstructor;

@AnalyzeClasses(packages = "deviceet.business", importOptions = DoNotIncludeTests.class)
public class AggregateRootArchTest {

    @ArchTest
    public static final ArchRule aggregateRootLocation = classes()
            .that()
            .areAssignableTo(AggregateRoot.class)
            .should()
            .resideInAnyPackage("deviceet.business..domain")
            .because("Aggregate root should located directly under domain package.");

    @ArchTest
    public static final ArchRule aggregateRootShouldBeAnnotatedWithTypeAlias = classes()
            .that()
            .areAssignableTo(AggregateRoot.class)
            .should()
            .beAnnotatedWith(TypeAlias.class)
            .because("Aggregate roots should be annotated with @TypeAlias as otherwise class FQCN will be used as type information and stored in MongoDB, which does not survive repackaging.");

    @ArchTest
    public static final ArchRule aggregateRootShouldHaveNoArgConstructor = classes()
            .that()
            .areAssignableTo(AggregateRoot.class)
            .should(havePrivateNoArgConstructor())
            .because("Private no arg constructors(you can use @NoArgsConstructor(access = PRIVATE)) of aggregate roots are only used for Jackson/Mongo deserialization, it should not be used to create objects because otherwise we might end up with invalid aggregate roots which are the most important types of objects in software.");

    @ArchTest
    public static final ArchRule aggregateRootShouldNotHaveBuilder = classes()
            .that()
            .areAssignableTo(AggregateRoot.class)
            .should(haveNoBuilderMethod())
            .because("Aggregate root should be created using explict constructors but not builders, otherwise we might end up with invalid objects.");

}
