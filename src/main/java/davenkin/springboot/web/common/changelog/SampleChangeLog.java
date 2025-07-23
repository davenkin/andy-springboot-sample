package davenkin.springboot.web.common.changelog;

import davenkin.springboot.web.user.domain.User;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@ChangeUnit(id = "sampleChangeUnit", order = "001", author = "davenkin")
public class SampleChangeLog {

    @Execution
    public void sampleExecute(MongoTemplate mongoTemplate) {
        mongoTemplate.save(new User("from changelog"));
    }

    @RollbackExecution
    public void sampleRollbackException(MongoTemplate mongoTemplate) {
        // roll back
        log.warn("Sample change log rolled back.");
    }
}
