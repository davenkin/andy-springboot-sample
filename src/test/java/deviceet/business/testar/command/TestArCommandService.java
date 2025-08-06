package deviceet.business.testar.command;

import deviceet.business.testar.domain.TestAr;
import deviceet.business.testar.domain.TestArFactory;
import deviceet.business.testar.domain.TestArRepository;
import deviceet.common.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static deviceet.common.security.Role.ORG_ADMIN;
import static deviceet.common.security.Role.ORG_IT_ADMIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArCommandService {
    private final TestArRepository testArRepository;
    private final TestArFactory testArFactory;

    @Transactional
    public String createTestAr(CreateTestArCommand command, Principal principal) {
        principal.checkRole(ORG_ADMIN);

        TestAr testAr = testArFactory.create(command.name(), principal);
        testArRepository.save(testAr);
        log.info("Created TestAr[{}].", testAr.getId());
        return testAr.getId();
    }

    @Transactional
    public void updateTestArName(String id, UpdateTestArNameCommand command, Principal principal) {
        principal.checkRole(ORG_IT_ADMIN);

        TestAr testAr = testArRepository.byId(id, principal.getOrgId());
        testAr.updateName(command.name());
        testArRepository.save(testAr);
        log.info("Updated name for TestAr[{}].", testAr.getId());
    }
}
