package deviceet.common.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@ChangeUnit(id = "sample", order = "001", author = "andy")
public class Migration001_Sample {

    @Execution
    public void sampleExecute(MongoTemplate mongoTemplate) {
        // use mongoTemplate to manipulate the database
        log.debug("Sample change log executed.");
    }

// By default Mongock is configured as transactional, hence no need to rollback as transactions manages rollback automatically

//    @RollbackExecution
//    public void sampleRollbackException(MongoTemplate mongoTemplate) {
//        // roll back
//        log.debug("Sample change log rolled back.");
//    }
}
