package deviceet.business.animal.job.task;

import deviceet.business.animal.domain.TestAr;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteAllAnimalsTask {
    public final MongoTemplate mongoTemplate;

    public void run() {
        mongoTemplate.remove(new Query(), TestAr.class);
    }

}
