package deviceet.business.testar;

import deviceet.business.testar.command.CreateTestArCommand;
import deviceet.business.testar.command.TestArCommandService;
import deviceet.business.testar.command.UpdateTestArNameCommand;
import deviceet.business.testar.query.ListTestArQuery;
import deviceet.business.testar.query.QListedTestAr;
import deviceet.business.testar.query.TestArQueryService;
import deviceet.common.security.Principal;
import deviceet.common.utils.ResponseId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.common.security.Role.ORG_ADMIN;
import static deviceet.common.utils.Constants.TEST_ORG_ID;
import static deviceet.common.utils.Constants.TEST_USER_ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/testars")
public class TestArController {
    //In real case, the Principal is usually build from some security context such as Spring Security's SecurityContextHolder
    public static final Principal TEST_PRINCIPAL = new Principal(TEST_USER_ID, "testAdminUser", ORG_ADMIN, TEST_ORG_ID);
    private final TestArCommandService testArCommandService;
    private final TestArQueryService testArQueryService;

    @PostMapping
    public ResponseId createTestAr(@RequestBody @Valid CreateTestArCommand command) {
        return new ResponseId(this.testArCommandService.createTestAr(command, TEST_PRINCIPAL));
    }

    @PutMapping(value = "/{testArId}/name")
    public void updateTestArName(@PathVariable("testArId") @NotBlank String testArId,
                                 @RequestBody @Valid UpdateTestArNameCommand updateTestArNameCommand) {
        this.testArCommandService.updateTestArName(testArId, updateTestArNameCommand, TEST_PRINCIPAL);
    }

    @PostMapping("/list")
    public Page<QListedTestAr> listTestArs(@RequestBody @Valid ListTestArQuery query,
                                           @PageableDefault Pageable pageable) {
        return this.testArQueryService.listTestArs(query, pageable, TEST_PRINCIPAL);
    }

}
