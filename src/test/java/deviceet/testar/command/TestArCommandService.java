package deviceet.testar.command;

import deviceet.common.security.Principal;
import deviceet.testar.domain.TestAr;
import deviceet.testar.domain.TestArFactory;
import deviceet.testar.domain.TestArRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArCommandService {
    private final TestArRepository testArRepository;
    private final TestArFactory testArFactory;

    @Transactional
    public String createTestAr(CreateTestArCommand command, Principal principal) {
        TestAr testAr = testArFactory.create(command.name(), principal);
        testArRepository.save(testAr);
        log.info("Created TestAr[{}].", testAr.getId());
        return testAr.getId();
    }

    @Transactional
    public void updateTestArName(String id, UpdateTestArNameCommand command, Principal principal) {
        TestAr testAr = testArRepository.byId(id, principal.getOrgId());
        testAr.updateName(command.name());
        testArRepository.save(testAr);
        log.info("Updated name for TestAr[{}].", testAr.getId());
    }
}
