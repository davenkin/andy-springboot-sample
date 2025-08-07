package deviceet.sample.equipment.job.task;

import deviceet.sample.equipment.domain.Equipment;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteAllAnimalsTask {
    public final MongoTemplate mongoTemplate;

    public void run() {
        mongoTemplate.remove(new Query(), Equipment.class);
    }

}
