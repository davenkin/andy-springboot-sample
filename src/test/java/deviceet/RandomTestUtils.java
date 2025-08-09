package deviceet;

import deviceet.common.model.principal.Role;
import deviceet.common.model.principal.UserPrincipal;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.UpdateEquipmentNameCommand;
import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.commons.lang3.RandomUtils.secure;

public class RandomTestUtils {

    public static String randomEquipmentName() {
        return "EQUIPMENT_NAME_" + RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserId() {
        return "USER_" + RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserName() {
        return "USER_NAME_" + RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomOrgId() {
        return "ORG_" + RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static Role randomRole() {
        return randomEnum(Role.class);
    }

    public static UserPrincipal randomUserPrincipal() {
        return UserPrincipal.of(randomUserId(), randomUserName(), randomRole(), randomOrgId());
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
