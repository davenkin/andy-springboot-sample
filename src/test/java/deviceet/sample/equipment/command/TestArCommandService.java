package deviceet.sample.equipment.command;

import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.TestAr;
import deviceet.sample.equipment.domain.TestArFactory;
import deviceet.sample.equipment.domain.TestArRepository;
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
