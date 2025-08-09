package deviceet.archunit;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "deviceet", importOptions = DoNotIncludeTests.class)
class PackageDependencyArchTest {

    @ArchTest
    static final ArchRule commonClassesNotDependOnBusinessPackages = noClasses()
            .that()
            .resideInAnyPackage("..deviceet.common..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "..deviceet.business..")
            .because("The common package is shard by all businesses, it should not depend on any specific business packages.");

    @ArchTest
    static final ArchRule businessClassesShouldAllUnderSpecificPackages = classes()
            .that()
            .resideInAnyPackage("..deviceet.business..")
            .should()
            .resideInAnyPackage(
                    "..deviceet.business..command..",
                    "..deviceet.business..controller..",
                    "..deviceet.business..domain..",
                    "..deviceet.business..eventhandler..",
                    "..deviceet.business..infrastructure..",
                    "..deviceet.business..job..",
                    "..deviceet.business..query.."
            )
            .because("We use the following packages to house all business classes: command, controller, domain, eventhandler, infrastructure, job, query.");

    @ArchTest
    static final ArchRule domainClassesShouldNotDependOnOuterPackages = noClasses()
            .that()
            .resideInAnyPackage("..deviceet.business..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "..deviceet.business..command..",
                    "..deviceet.business..eventhandler..",
                    "..deviceet.business..infrastructure..",
                    "..deviceet.business..job..",
                    "..deviceet.business..query..")
            .because("Domain package is most important part of the application and reside in the kernel of the architecture, it should only contain business logic and  should not depend on other outer packages.");

}
