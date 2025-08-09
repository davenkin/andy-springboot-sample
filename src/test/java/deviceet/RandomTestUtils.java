package deviceet;

import deviceet.common.model.Principal;
import deviceet.common.model.Role;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.UpdateEquipmentNameCommand;
import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.commons.lang3.RandomUtils.secure;

public class RandomTestUtils {

    public static String randomEquipmentName() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserId() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserName() {
        return RandomStringUtils.secure().nextAlphanumeric(5) + ":User";
    }

    public static String randomOrgId() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static Role randomRole() {
        return randomEnum(Role.class);
    }

    public static Principal randomPrincipal() {
        return new Principal(randomUserId(), randomUserName(), randomRole(), randomOrgId());
    }

    public static CreateEquipmentCommand randomCreateEquipmentCommand() {
        return new CreateEquipmentCommand(randomEquipmentName());
    }

    public static UpdateEquipmentNameCommand randomUpdateEquipmentNameCommand() {
        return new UpdateEquipmentNameCommand(randomEquipmentName());
    }

    public static <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        return constants[secure().randomInt(0, constants.length)];
    }
}
