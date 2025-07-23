package davenkin.springboot.web.department;

import davenkin.springboot.web.BaseApiTest;
import davenkin.springboot.web.common.utils.ResponseId;
import davenkin.springboot.web.department.command.CreateDepartmentCommand;
import davenkin.springboot.web.department.domain.Department;
import davenkin.springboot.web.department.domain.event.DepartmentCreatedEvent;
import org.junit.jupiter.api.Test;

import static davenkin.springboot.web.common.domainevent.DomainEventType.DEPARTMENT_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class DepartmentControllerApiTest extends BaseApiTest {

    @Test
    public void should_create_department() throws InterruptedException {
        CreateDepartmentCommand command = CreateDepartmentCommand.builder().name("finance").build();
        ResponseId responseId = this.webTestClient.post()
                .uri("/departments")
                .body(fromValue(command))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(ResponseId.class)
                .returnResult()
                .getResponseBody();
        Department user = departmentRepository.byId(responseId.id());
        assertEquals(command.name(), user.getName());

        DepartmentCreatedEvent departmentCreatedEvent = latestEventFor(user.getId(), DEPARTMENT_CREATED, DepartmentCreatedEvent.class);
        assertEquals(user.getId(), departmentCreatedEvent.getArId());
        Thread.sleep(3000); // sleep some time for events to publish, normally you won't do this
    }
}
