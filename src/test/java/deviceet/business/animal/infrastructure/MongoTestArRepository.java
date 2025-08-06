package deviceet.business.animal.infrastructure;

import deviceet.business.animal.domain.TestAr;
import deviceet.business.animal.domain.TestArRepository;
import deviceet.common.infrastructure.AbstractMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTestArRepository extends AbstractMongoRepository<TestAr> implements TestArRepository {
}
