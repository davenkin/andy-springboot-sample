package deviceet.common.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@ChangeUnit(id = "sampleChangeUnit", order = "001", author = "andy")
public class Migration001_SampleChangeUnit {

    @Execution
    public void sampleExecute(MongoTemplate mongoTemplate) {
        // use mongoTemplate to manipulate the database
        log.debug("Sample change log executed.");
    }

    @RollbackExecution
    public void sampleRollbackException(MongoTemplate mongoTemplate) {
        // roll back
        log.debug("Sample change log rolled back.");
    }
}
