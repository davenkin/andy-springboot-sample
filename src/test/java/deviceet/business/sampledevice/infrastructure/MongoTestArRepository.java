package deviceet.business.sampledevice.infrastructure;

import deviceet.business.sampledevice.domain.TestAr;
import deviceet.business.sampledevice.domain.TestArRepository;
import deviceet.common.infrastructure.AbstractMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTestArRepository extends AbstractMongoRepository<TestAr> implements TestArRepository {
}
