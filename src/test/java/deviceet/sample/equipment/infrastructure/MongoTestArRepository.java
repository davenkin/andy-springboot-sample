package deviceet.sample.equipment.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.sample.equipment.domain.TestAr;
import deviceet.sample.equipment.domain.TestArRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTestArRepository extends AbstractMongoRepository<TestAr> implements TestArRepository {
}
