package deviceet.business.testar.job.task;

import deviceet.business.testar.job.TestArJob;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteAllTestArsTask {
    public final MongoTemplate mongoTemplate;

    public void run() {
        mongoTemplate.remove(new Query(), TestArJob.class);
    }

}
