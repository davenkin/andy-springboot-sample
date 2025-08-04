package deviceet.business.testar.infrastructure;

import deviceet.business.testar.domain.TestAr;
import deviceet.business.testar.domain.TestArRepository;
import deviceet.common.infrastructure.AbstractMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTestArRepository extends AbstractMongoRepository<TestAr> implements TestArRepository {
}
