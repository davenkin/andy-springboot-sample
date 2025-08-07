package deviceet.sample.equipment;

import deviceet.common.utils.ResponseId;
import deviceet.sample.equipment.command.CreateTestArCommand;
import deviceet.sample.equipment.command.TestArCommandService;
import deviceet.sample.equipment.command.UpdateTestArNameCommand;
import deviceet.sample.equipment.query.ListTestArQuery;
import deviceet.sample.equipment.query.QListedTestAr;
import deviceet.sample.equipment.query.TestArQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.TestUtils.TEST_PRINCIPAL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/testars")
public class TestArController {
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
