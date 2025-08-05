package deviceet.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;

public class ArchUnitUtils {
    public static ArchCondition<JavaClass> havePrivateNoArgConstructor() {
        return new ArchCondition<>("have a private no-arg constructor") {
            @Override
            public void check(JavaClass clazz, ConditionEvents events) {
                boolean hasPrivateNoArgCtor = clazz.getConstructors().stream()
                        .anyMatch(ctor -> ctor.getParameters().isEmpty() && ctor.getModifiers().contains(PRIVATE));
                String message = hasPrivateNoArgCtor
                        ? String.format("Class %s has a private no-arg constructor.", clazz.getName())
                        : String.format("Class %s does not have a private no-arg constructor.", clazz.getName());

                events.add(new SimpleConditionEvent(clazz, hasPrivateNoArgCtor, message));
            }
        };
    }
}
